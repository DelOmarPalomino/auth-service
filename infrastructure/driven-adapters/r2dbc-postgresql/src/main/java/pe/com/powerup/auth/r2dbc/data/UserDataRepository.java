package pe.com.powerup.auth.r2dbc.data;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface UserDataRepository extends
        ReactiveCrudRepository<UserData, Long>,
        ReactiveQueryByExampleExecutor<UserData> {

    // Derived query (no @Query necesario)
    Mono<Boolean> existsByEmail(String email);
}
