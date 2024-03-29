package com.blackphoenixproductions.forumbackend.config.security;

import org.keycloak.adapters.springsecurity.KeycloakConfiguration;
import org.keycloak.adapters.springsecurity.authentication.KeycloakAuthenticationProvider;
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.web.cors.CorsConfiguration;

@KeycloakConfiguration
class SecurityConfig extends KeycloakWebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(
            AuthenticationManagerBuilder auth) throws Exception {

        KeycloakAuthenticationProvider keycloakAuthenticationProvider
                = keycloakAuthenticationProvider();
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
                new SimpleAuthorityMapper());
        auth.authenticationProvider(keycloakAuthenticationProvider);
    }

    @Bean
    @Override
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(
                new SessionRegistryImpl());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.authorizeRequests()
                .antMatchers("/api/getTotalUsers").permitAll()
                .antMatchers("/api/getTotalTopics").permitAll()
                .antMatchers("/api/findTopicsByPage").permitAll()
                .antMatchers("/api/findFilteredTopicsByPage").permitAll()
                .antMatchers("/api/findTopic").permitAll()
                .antMatchers("/api/getTotalPosts").permitAll()
                .antMatchers("/api/findPostsByPage").permitAll()
                .antMatchers("/api/getBuildVersionBackEnd").permitAll()
                .antMatchers("/swagger-ui/*").hasAnyRole("STAFF", "HELPDESK")
                .anyRequest()
                .authenticated();
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues());
        http.csrf().disable();
    }
}
