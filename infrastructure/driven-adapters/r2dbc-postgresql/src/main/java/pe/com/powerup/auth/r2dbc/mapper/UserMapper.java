package pe.com.powerup.auth.r2dbc.mapper;

import pe.com.powerup.auth.model.user.User;
import pe.com.powerup.auth.r2dbc.data.UserData;

public final class UserMapper {
    private UserMapper() {
    }

    // Dominio -> Data (para guardar). OJO: no seteamos el id; lo genera Postgres.
    public static UserData toData(User u) {
        if (u == null)
            return null;
        return UserData.builder()
                .firstName(u.getFirstName())
                .lastName(u.getLastName())
                .email(u.getEmail())
                .phone(u.getPhone())
                .address(u.getAddress())
                .birthDate(u.getBirthDate())
                .baseSalary(u.getBaseSalary())
                .build();
    }

    // Data -> Dominio (para devolver al caso de uso/API). Convertimos Long ->
    // String.
    public static User toDomain(UserData d) {
        if (d == null)
            return null;
        return User.builder()
                .id(d.getId() == null ? null : d.getId().toString())
                .firstName(d.getFirstName())
                .lastName(d.getLastName())
                .email(d.getEmail())
                .phone(d.getPhone())
                .address(d.getAddress())
                .birthDate(d.getBirthDate())
                .baseSalary(d.getBaseSalary())
                .build();
    }
}