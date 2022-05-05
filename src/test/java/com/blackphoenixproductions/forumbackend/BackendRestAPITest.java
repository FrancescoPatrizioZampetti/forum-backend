package com.blackphoenixproductions.forumbackend;


import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
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
    public void test_signin_restTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        User userDTO = new User();
        userDTO.setUsername("ciao_mondo");
        userDTO.setEmail("test@test.com");

        HttpEntity<User> request = new HttpEntity<>(userDTO, headers);
        // se dovessi in aggiunta mettere dei query param dovrei inserirli nella url, vedi UriComponentsBuilder

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EntityModel<User>> resp = null;
        try {
            resp = restTemplate.exchange(BACKEND_PATH + "/api/signin", HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<User>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(resp);
    }


    @Test
    public void test_findTopic_restTemplate() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("id", "1");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<EntityModel<Topic>> response = null;
        try {
            response = restTemplate.exchange(BACKEND_PATH + "/api/findTopic", HttpMethod.POST, request, new ParameterizedTypeReference<EntityModel<Topic>>(){});
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(response);
    }



}
