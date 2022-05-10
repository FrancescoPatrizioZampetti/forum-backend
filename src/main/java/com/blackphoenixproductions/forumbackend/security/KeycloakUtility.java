package com.blackphoenixproductions.forumbackend.security;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class KeycloakUtility {

    public static Set<String> getRoles(HttpServletRequest request){
        AccessToken accessToken = getAccessToken(request);
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

}
