package com.example.demo.configurations;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Product Management APIs", 
								version = "1.0.0", 
								description = "Microservices for Product and Inventory Management",
								termsOfService = "Terms and conditions applied", 
								contact = @Contact(name = "Piyush Garg", email = "piyush-garg@hcltech.com", url = "piyush-garg.com"), 
								license = @License(name = "Piyush License")), 
								servers = {
										@Server(description = "devServer", url = "http://localhost:8080"),
										@Server(description = "testServer", url = "http://localhost:8080"),
								}, 
								security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(name = "bearerAuth", scheme = "bearer", type = SecuritySchemeType.HTTP, description = "JWT Bearer authentication", bearerFormat = "JWT")
public class SwaggerConfig {
	
}