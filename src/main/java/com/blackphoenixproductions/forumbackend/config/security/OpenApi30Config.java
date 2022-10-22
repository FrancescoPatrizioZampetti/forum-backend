package com.blackphoenixproductions.forumbackend.config.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Forum API", version = "v1", description = "Le API del forum.", contact = @Contact(email = "zampetti1@hotmail.com")))
public class OpenApi30Config {
}
