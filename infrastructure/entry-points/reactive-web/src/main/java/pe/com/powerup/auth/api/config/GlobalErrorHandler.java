package pe.com.powerup.auth.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import pe.com.powerup.auth.api.dto.ErrorResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2) // se ejecuta antes del default handler de Spring
public class GlobalErrorHandler implements ErrorWebExceptionHandler {

    // ObjectMapper local (sin inyectar) para evitar wiring innecesario
    private final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        HttpStatus status;
        ErrorResponse body;

        if (ex instanceof IllegalArgumentException iae) {
            status = HttpStatus.BAD_REQUEST;
            body = new ErrorResponse("VALIDATION_ERROR", iae.getMessage());
        } else if (ex instanceof IllegalStateException ise) {
            status = HttpStatus.CONFLICT;
            body = new ErrorResponse("ALREADY_EXISTS", ise.getMessage());
        } else if (ex instanceof ServerWebInputException) { // JSON mal formado, tipos inválidos, etc.
            status = HttpStatus.BAD_REQUEST;
            body = new ErrorResponse("INVALID_REQUEST", "El cuerpo o formato de la solicitud es inválido.");
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            body = new ErrorResponse("UNEXPECTED_ERROR", "Ha ocurrido un error no esperado.");
        }

        // Traza centralizada (el cid ya lo pone tu RequestLoggingFilter)
        log.error("Unhandled error -> {}", ex.toString(), ex);

        var response = exchange.getResponse();
        response.setStatusCode(status);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        try {
            var bytes = mapper.writeValueAsBytes(body);
            return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
        } catch (Exception writeErr) {
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return response.setComplete();
        }
    }
}