package com.blackphoenixproductions.forumbackend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.cors.CorsConfiguration;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception
    {
        // cors
        http.cors().configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
                .and().
        // Disabilita CSRF (cross site request forgery)
        csrf().disable();

        // Nessuna sessione verrà creata o usata da spring security
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Entry points
        http.authorizeRequests()//
                .antMatchers("/api/login").permitAll()//  accesso normale
                .antMatchers("/api/loginFacebook").permitAll()//  accesso tramite Facebook
                .antMatchers("/api/loginGoogle").permitAll()//  accesso tramite Google
                .antMatchers("/api/signin").permitAll()// registrazione
                .antMatchers("/api/getTotalUsers").permitAll()
                .antMatchers("/api/getTotalTopics").permitAll()
                .antMatchers("/api/findTopicsByPage").permitAll()
                .antMatchers("/api/findFilteredTopicsByPage").permitAll()
                .antMatchers("/api/findTopic").permitAll()
                .antMatchers("/api/getTotalPosts").permitAll()
                .antMatchers("/api/findPostsByPage").permitAll()
                .antMatchers("/api/getBuildVersionBackEnd").permitAll()
                .antMatchers("/api/initResetCredentials").permitAll() // init procedura di reset password
                .antMatchers("/api/finishResetCredentials").permitAll() // end procedura di reset password
                // swagger
                .antMatchers("/v3/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/swagger-ui.html").permitAll()
                .antMatchers("/swagger-ui/*").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/v3/**").permitAll()
                // actuator
                .antMatchers("/actuator/**").permitAll()
                // Accesso con authentication per tutti gli altri
                .anyRequest().authenticated();

        // Apply JWT
        http.apply(new JwtTokenFilterConfigurer(jwtTokenProvider));

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}