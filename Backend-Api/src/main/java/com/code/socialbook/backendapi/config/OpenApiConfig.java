package com.code.socialbook.backendapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Yves HAKIZIMANA",
                        email = "yhakizimana@rca.ac.rw",
                        url = "https://rca.ac.rw"
                ),
                description = "OpenApi Documentation for Spring Security.",
                title = "OpenApi Specification - Yves",
                version = "1.0",
                license = @License(
                        name = "RCA-License",
                        url = "https://rca.ac.rw"
                ),
                termsOfService = "Terms of Service."
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/api/v1"
                ),
                @Server(description = "Prod ENV", url = "http://rca.ac.rw/testing")
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
