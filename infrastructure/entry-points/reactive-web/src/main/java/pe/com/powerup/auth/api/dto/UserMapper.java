package pe.com.powerup.auth.api.dto;

import pe.com.powerup.auth.model.user.User;

public final class UserMapper {
    private UserMapper() {
    }

    public static User toDomain(UserRequest r) {
        return User.builder()
                .firstName(r.firstName())
                .lastName(r.lastName())
                .email(r.email())
                .phone(r.phone())
                .address(r.address())
                .birthDate(r.birthDate())
                .baseSalary(r.baseSalary())
                .build();
    }
}
