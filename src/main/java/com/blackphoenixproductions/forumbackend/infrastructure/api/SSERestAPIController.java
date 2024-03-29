package com.blackphoenixproductions.forumbackend.infrastructure.api;

import com.blackphoenixproductions.forumbackend.domain.ISSEPushNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@RestController
@RequestMapping("/api")
@Tag(name = "5. SSE", description = "endpoints riguardanti gli SSE.")
public class SSERestAPIController {

    private final ISSEPushNotificationService service;
    private static final Logger logger = LoggerFactory.getLogger(SSERestAPIController.class);

    @Autowired
    public SSERestAPIController(ISSEPushNotificationService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "403", description = "Forbidden.", content = @Content(schema = @Schema(hidden = true))),
    })
    @Operation(summary = "Endpoint per la sottoscrizione delle notifiche push.", hidden = true)
    @GetMapping("/subscribe")
    public ResponseEntity<SseEmitter> subscribe(@RequestParam String username){
        // 60000L * 5 = 5 min
        final SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        try {
            logger.info("Subscribing to SSE : {}", username);
            service.addEmitter(username, emitter);
            service.initEvent(username);
            emitter.onCompletion(() -> service.removeEmitter(emitter));
            emitter.onTimeout(() -> service.removeEmitter(emitter));
            emitter.onError((e) -> service.removeEmitter(emitter));
        } catch (Exception e){
            logger.warn(e.getMessage());
        }
        return new ResponseEntity<>(emitter, HttpStatus.OK);
    }


    /**
     * Ogni 30 secondi manda un messaggio vuoto per tenere aperta la connessione sse
     */
    @Scheduled(fixedRate = 30000)
    public void sendHeartBeat(){
        service.sendHeartBeatEvent();
    }

}
