package com.siga.sus.authorization.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${spring.application.host}")
    private String host;

    @Value("${server.port}")
    private String port;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Value("${spring.application.version}")
    private String version;

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("SIGA SUS - Authorization Service API")
                        .description("API de autenticação e autorização para o sistema SIGA SUS")
                        .version(version)
                        .contact(new Contact()
                                .name("SIGA SUS Team")
                                .email("contato@sigasus.com")
                                .url("https://sigasus.com")))
                .servers(List.of(
                        new Server()
                                .url(host + port + contextPath)))
                .addSecurityItem(new SecurityRequirement()
                        .addList(securitySchemeName));
    }
}
