package pe.com.powerup.auth.model.user;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class User {
    String id; // lo asignará la infraestructura
    String firstName;
    String lastName;
    String email;
    String phone;
    String address;
    LocalDate birthDate;
    BigDecimal baseSalary;
}
