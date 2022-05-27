package com.blackphoenixproductions.forumbackend.api;

import com.blackphoenixproductions.forumbackend.dto.KeyValueDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
@Tag(name = "6. Value", description = "endpoints riguardanti le informazioni generiche dell'applicativo.")
public class ValueRestAPIController {

    private final String buildVersion;

    @Autowired
    public ValueRestAPIController(@Value("${build.version}") String buildVersion) {
        this.buildVersion = buildVersion;
    }


    @Operation(summary = "Restituisce la versione del forum-backend.", hidden = true)
    @GetMapping(value = "/getBuildVersionBackEnd")
    public ResponseEntity<KeyValueDTO> getBuildVersionBackEnd (HttpServletRequest req){
        return new ResponseEntity<KeyValueDTO>(new KeyValueDTO("buildVersion", buildVersion), HttpStatus.OK);
    }
}
