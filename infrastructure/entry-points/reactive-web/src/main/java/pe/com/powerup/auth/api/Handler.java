package pe.com.powerup.auth.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import pe.com.powerup.auth.api.dto.UserMapper;
import pe.com.powerup.auth.api.dto.UserRequest;
import pe.com.powerup.auth.usecase.registeruser.RegisterUserUseCase;

@Slf4j
@Component
@RequiredArgsConstructor
public class Handler {

    private final RegisterUserUseCase registerUserUseCase;

    public Mono<ServerResponse> registerUser(ServerRequest request) {
        return request.bodyToMono(UserRequest.class)
                .doOnNext(r -> log.debug("Handler.register payload={}", r))
                .map(UserMapper::toDomain)
                .flatMap(registerUserUseCase::execute)
                .doOnNext(saved -> log.debug("Handler.register saved id={}", saved.getId()))
                .flatMap(saved -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(saved));
    }

}
