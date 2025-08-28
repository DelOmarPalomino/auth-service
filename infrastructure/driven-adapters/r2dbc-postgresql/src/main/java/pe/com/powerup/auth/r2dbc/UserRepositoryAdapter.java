package pe.com.powerup.auth.r2dbc;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;

import pe.com.powerup.auth.model.user.User;
import pe.com.powerup.auth.model.user.gateways.UserRepository;
import pe.com.powerup.auth.r2dbc.data.UserData;
import pe.com.powerup.auth.r2dbc.data.UserDataRepository;
import pe.com.powerup.auth.r2dbc.mapper.UserMapper;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {

    private final UserDataRepository repository;
    private final TransactionalOperator tx;

    @Override
    public Mono<Boolean> existsByEmail(String email) {
        return repository.existsByEmail(email);
    }

    @Override
    public Mono<User> save(User user) {
        UserData data = UserMapper.toData(user);
        return tx.transactional(
                repository.save(data)
                        .map(UserMapper::toDomain))
                .onErrorMap(DataIntegrityViolationException.class,
                        ex -> new IllegalStateException("email ya registrado", ex));
    }

}