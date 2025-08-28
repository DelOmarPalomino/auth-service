package pe.com.powerup.auth.usecase.registeruser;

import lombok.RequiredArgsConstructor;
import pe.com.powerup.auth.model.common.DomainLogger;
import pe.com.powerup.auth.model.user.User;
import pe.com.powerup.auth.model.user.gateways.UserRepository;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final DomainLogger log;

    private static final BigDecimal MIN_SALARY = BigDecimal.ZERO;
    private static final BigDecimal MAX_SALARY = new BigDecimal("1500000");
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Mono<User> execute(User candidate) {
        return Mono.just(candidate)
                .map(this::normalize)
                .doOnNext(u -> log.debug("UC.normalize -> {}", u))
                .flatMap(this::validate)
                .doOnNext(u -> log.debug("UC.validate OK -> {}", u.getEmail()))
                .flatMap(this::ensureEmailNotTaken)
                .flatMap(userRepository::save)
                .doOnNext(saved -> log.info("UC.saved -> email={}, id={}", saved.getEmail(), saved.getId()));
    }

    // ---------- helpers de dominio ----------
    private User normalize(User u) {
        return u.toBuilder()
                .firstName(trim(u.getFirstName()))
                .lastName(trim(u.getLastName()))
                .email(lower(trim(u.getEmail())))
                .phone(trim(u.getPhone()))
                .address(trim(u.getAddress()))
                .build();
    }

    private Mono<User> validate(User u) {
        if (isBlank(u.getFirstName()))
            return Mono.error(new IllegalArgumentException("firstName es obligatorio"));
        if (isBlank(u.getLastName()))
            return Mono.error(new IllegalArgumentException("lastName es obligatorio"));
        if (isBlank(u.getEmail()))
            return Mono.error(new IllegalArgumentException("email es obligatorio"));
        if (u.getBaseSalary() == null)
            return Mono.error(new IllegalArgumentException("baseSalary es obligatorio"));

        if (!EMAIL_REGEX.matcher(u.getEmail()).matches())
            return Mono.error(new IllegalArgumentException("email con formato inválido"));

        if (u.getBaseSalary().compareTo(MIN_SALARY) < 0
                || u.getBaseSalary().compareTo(MAX_SALARY) > 0)
            return Mono.error(new IllegalArgumentException("baseSalary fuera de rango (0 .. 1'500,000)"));

        if (u.getBirthDate() != null && u.getBirthDate().isAfter(LocalDate.now()))
            return Mono.error(new IllegalArgumentException("birthDate no puede ser futura"));

        return Mono.just(u);
    }

    private Mono<User> ensureEmailNotTaken(User u) {
        return userRepository.existsByEmail(u.getEmail())
                .flatMap(exists -> exists
                        ? Mono.error(new IllegalStateException("email ya registrado"))
                        : Mono.just(u));
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }

    private static String lower(String s) {
        return s == null ? null : s.toLowerCase();
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
