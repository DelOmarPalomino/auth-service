package pe.com.powerup.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.*;

import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import pe.com.powerup.auth.api.dto.UserRequest;
import pe.com.powerup.auth.api.dto.ErrorResponse;
import pe.com.powerup.auth.model.user.User;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

        private final Handler handler;

        @Bean
        @RouterOperations({
                        @RouterOperation(path = "/api/v1/usuarios", method = RequestMethod.POST, beanClass = Handler.class, beanMethod = "registerUser", operation = @Operation(operationId = "createUser", summary = "Registrar usuario", description = "Crea un nuevo usuario si el email no está registrado.", tags = {
                                        "Usuarios" }, requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = UserRequest.class))), responses = {
                                                        @ApiResponse(responseCode = "201", description = "Creado", content = @Content(schema = @Schema(implementation = User.class))),
                                                        @ApiResponse(responseCode = "400", description = "Validación", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                                                        @ApiResponse(responseCode = "409", description = "Duplicado", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
                                        }))
        })
        public RouterFunction<ServerResponse> routes() {
                return RouterFunctions.route()
                                .POST("/api/v1/usuarios",
                                                RequestPredicates.accept(MediaType.APPLICATION_JSON),
                                                handler::registerUser)
                                .build();
        }
}
