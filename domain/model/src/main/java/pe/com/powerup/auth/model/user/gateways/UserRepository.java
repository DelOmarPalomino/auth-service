package pe.com.powerup.auth.model.user.gateways;

import pe.com.powerup.auth.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Boolean> existsByEmail(String email);

    Mono<User> save(User user);
}
