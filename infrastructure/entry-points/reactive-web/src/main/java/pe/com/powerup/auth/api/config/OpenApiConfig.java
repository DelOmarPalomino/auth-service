package pe.com.powerup.auth.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "AuthService API", version = "v1", description = "API para registro de usuarios"))
public class OpenApiConfig {
}
