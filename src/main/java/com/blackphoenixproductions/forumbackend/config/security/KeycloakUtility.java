package com.blackphoenixproductions.forumbackend.config.security;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Set;

public class KeycloakUtility {

    private final static String BEARER = "Bearer ";

    public static Set<String> getRoles(HttpServletRequest request){
        AccessToken accessToken = getAccessToken(request);
        AccessToken.Access realmAccess = accessToken.getRealmAccess();
        return realmAccess.getRoles();
    }

    public static Set<String> getRoles(AccessToken accessToken){
        AccessToken.Access realmAccess = accessToken.getRealmAccess();
        return realmAccess.getRoles();
    }

    public static AccessToken getAccessToken(HttpServletRequest request) {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) request.getUserPrincipal();
        KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
        KeycloakSecurityContext session = principal.getKeycloakSecurityContext();
        AccessToken accessToken = session.getToken();
        return accessToken;
    }

    public static String getAccessTokenString(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        String token = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();
        return token;
    }

    public static String getBearerTokenString(Principal principal) {
        KeycloakAuthenticationToken keycloakAuthenticationToken = (KeycloakAuthenticationToken) principal;
        String token = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getTokenString();
        return BEARER + token;
    }

}
