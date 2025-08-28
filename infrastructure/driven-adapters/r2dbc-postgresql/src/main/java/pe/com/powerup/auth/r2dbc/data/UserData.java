package pe.com.powerup.auth.r2dbc.data;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserData {
    @Id
    private Long id;

    @Column("first_name")
    private String firstName;
    @Column("last_name")
    private String lastName;
    @Column("email")
    private String email;
    @Column("phone")
    private String phone;
    @Column("address")
    private String address;
    @Column("birth_date")
    private LocalDate birthDate;
    @Column("base_salary")
    private BigDecimal baseSalary;
}