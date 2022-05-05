package com.blackphoenixproductions.forumbackend;

import dto.SimpleTopicDTO;
import dto.TokenContainerDTO;
import dto.UserDTO;
import dto.openApi.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


import static org.junit.jupiter.api.Assertions.*;



/**
 * Integration Tests API
 */
@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:/application-test.properties")
public class BackendRestAPITest {

    private static final String BACKEND_PATH = "http://localhost:8083";
    private WebClient webClient;


    @BeforeEach
    public void loadBeforeAllTests(){
        webClient = WebClient.create(BACKEND_PATH);
    }

    @Test
    public void test_login() {
        String jwtToken = null;
        try {
            jwtToken = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/api/login")
                            .queryParam("username", "admin")
                            .queryParam("password", "test1234")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class).block();
        } catch (Exception e) {
            WebClientResponseException errorResponse = (WebClientResponseException ) e;
            if(errorResponse.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)){
                throw new CustomException("Utente non presente nel sistema.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            e.printStackTrace();
        }
        assertNotNull(jwtToken);
    }


    @Test
    public void test_login_restTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("username", "admin");
        map.add("password", "test1234");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EntityModel<TokenContainerDTO>> tokenResponse = null;
        try {
            tokenResponse = restTemplate.exchange(BACKEND_PATH + "/api/login", HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<TokenContainerDTO>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(tokenResponse);
    }


    @Test
    public void test_signin_restTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("ciao_mondo");
        userDTO.setPassword("test1234");
        userDTO.setEmail("test@test.com");

        HttpEntity<UserDTO> request = new HttpEntity<>(userDTO, headers);
        // se dovessi in aggiunta mettere dei query param dovrei inserirli nella url, vedi UriComponentsBuilder

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EntityModel<TokenContainerDTO>> tokenResponse = null;
        try {
            tokenResponse = restTemplate.exchange(BACKEND_PATH + "/api/signin", HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<TokenContainerDTO>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(tokenResponse);
    }


    @Test
    public void test_findTopic_restTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("id", "1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EntityModel<SimpleTopicDTO>> response = null;
        try {
            response = restTemplate.exchange(BACKEND_PATH + "/api/findTopic", HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<SimpleTopicDTO>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(response);
    }



}
