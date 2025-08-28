package pe.com.powerup.auth.usecase.registeruser;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import pe.com.powerup.auth.model.common.DomainLogger;
import pe.com.powerup.auth.model.user.User;
import pe.com.powerup.auth.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserUseCaseTest {

    static class FakeRepo implements UserRepository {
        boolean exists = false;
        User lastSaved;

        public Mono<Boolean> existsByEmail(String email) {
            return Mono.just(exists);
        }

        public Mono<User> save(User user) {
            lastSaved = user.toBuilder().id("1").build();
            return Mono.just(lastSaved);
        }
    }

    static class NoopLog implements DomainLogger {
        public void debug(String msg, Object... args) {
        }

        public void info(String msg, Object... args) {
        }
    }

    private final FakeRepo repo = new FakeRepo();
    private final RegisterUserUseCase useCase = new RegisterUserUseCase(repo, new NoopLog());

    private static User valid() {
        return User.builder()
                .firstName(" Juan ")
                .lastName(" Pérez ")
                .email("USER@MAIL.COM ")
                .phone(" 999 ")
                .address(" Lima ")
                .birthDate(LocalDate.of(1990, 1, 1))
                .baseSalary(new BigDecimal("1000"))
                .build();
    }

    @Test
    void happyPath_persisteYNormaliza() {
        StepVerifier.create(useCase.execute(valid()))
                .assertNext(u -> {
                    assertEquals("1", u.getId());
                    assertEquals("user@mail.com", u.getEmail());
                    assertEquals("Juan", u.getFirstName());
                    assertEquals("Pérez", u.getLastName());
                })
                .verifyComplete();
    }

    @Test
    void faltaFirstName() {
        var in = valid().toBuilder().firstName("  ").build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("firstName es obligatorio", e.getMessage());
                }).verify();
    }

    @Test
    void faltaLastName() {
        var in = valid().toBuilder().lastName(null).build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("lastName es obligatorio", e.getMessage());
                }).verify();
    }

    @Test
    void faltaEmail() {
        var in = valid().toBuilder().email(" ").build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("email es obligatorio", e.getMessage());
                }).verify();
    }

    @Test
    void emailInvalido() {
        var in = valid().toBuilder().email("malformato").build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("email con formato inválido", e.getMessage());
                }).verify();
    }

    @Test
    void baseSalaryNulo() {
        var in = valid().toBuilder().baseSalary(null).build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("baseSalary es obligatorio", e.getMessage());
                }).verify();
    }

    @Test
    void baseSalaryFueraDeRango() {
        var in = valid().toBuilder().baseSalary(new BigDecimal("2000000")).build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("baseSalary fuera de rango (0 .. 1'500,000)", e.getMessage());
                }).verify();
    }

    @Test
    void birthDateFutura() {
        var in = valid().toBuilder().birthDate(LocalDate.now().plusDays(1)).build();
        StepVerifier.create(useCase.execute(in))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalArgumentException);
                    assertEquals("birthDate no puede ser futura", e.getMessage());
                }).verify();
    }

    @Test
    void emailDuplicado() {
        repo.exists = true;
        StepVerifier.create(useCase.execute(valid()))
                .expectErrorSatisfies(e -> {
                    assertTrue(e instanceof IllegalStateException);
                    assertEquals("email ya registrado", e.getMessage());
                }).verify();
        repo.exists = false;
    }
}