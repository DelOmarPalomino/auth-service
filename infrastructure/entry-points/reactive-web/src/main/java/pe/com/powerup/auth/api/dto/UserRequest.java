package pe.com.powerup.auth.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String phone,
        String address,
        LocalDate birthDate,
        BigDecimal baseSalary) {
}
