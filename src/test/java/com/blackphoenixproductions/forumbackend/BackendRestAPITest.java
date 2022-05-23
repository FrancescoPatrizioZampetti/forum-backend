package com.blackphoenixproductions.forumbackend;


import com.blackphoenixproductions.forumbackend.entity.Topic;
import com.blackphoenixproductions.forumbackend.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;


import javax.ws.rs.core.Response;

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
    public void test_keycloak_rest(){
        UriComponents builder = UriComponentsBuilder
                .fromHttpUrl("http://localhost:8080/realms/forum/protocol/openid-connect/token")
                .build();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", "forum-client");
        body.add("realm", "forum");
        body.add("username", "helpdesk");
        body.add("password", "test1234");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        AccessTokenResponse accessTokenResponse = restTemplate.postForObject(builder.toUri(), request, AccessTokenResponse.class);

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setContentType(MediaType.APPLICATION_JSON);
        headers2.setBearerAuth(accessTokenResponse.getToken());

        HttpEntity<MultiValueMap<String, String>> request2 = new HttpEntity<>(headers2);

        ResponseEntity<UserRepresentation> resp = null;
        try {
            resp = restTemplate.exchange("http://localhost:8080/admin/realms/forum/users/6625043e-2c1b-46e0-bc95-c8ffa3deee9a", HttpMethod.GET, request2, UserRepresentation.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        UserRepresentation user = resp.getBody();
        user.setUsername("test-user3");

        HttpEntity<Object> request3 = new HttpEntity<>(user, headers2);

        ResponseEntity<Void> resp2 = null;
        try {
            resp2 = restTemplate.exchange("http://localhost:8080/admin/realms/forum/users/6625043e-2c1b-46e0-bc95-c8ffa3deee9a", HttpMethod.PUT, request3, Void.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(resp2.getStatusCode().is2xxSuccessful()) {
            System.out.println("Username cambiato!");
        }

    }

   @Test
   public void test_keycloak_builder(){
       Keycloak kc = KeycloakBuilder.builder()
               .serverUrl("http://localhost:8080/")
               .realm("forum")
               .username("helpdesk")
               .password("test1234")
               .clientId("forum-client")
               .build();

       UserRepresentation user = kc.realm("forum").users().get("6625043e-2c1b-46e0-bc95-c8ffa3deee9a").toRepresentation();
       user.setUsername("test-ciccio_2");
       kc.realm("forum").users().get("6625043e-2c1b-46e0-bc95-c8ffa3deee9a").update(user);

       UserRepresentation updatedUser = kc.realm("forum").users().get("6625043e-2c1b-46e0-bc95-c8ffa3deee9a").toRepresentation();
       updatedUser.getUsername();

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
