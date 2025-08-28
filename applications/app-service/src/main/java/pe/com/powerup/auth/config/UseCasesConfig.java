package pe.com.powerup.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pe.com.powerup.auth.model.common.DomainLogger;
import pe.com.powerup.auth.model.user.gateways.UserRepository;
import pe.com.powerup.auth.usecase.registeruser.RegisterUserUseCase;

@Configuration
public class UseCasesConfig {

        @Bean
        public RegisterUserUseCase registerUserUseCase(UserRepository repo, DomainLogger log) {
                return new RegisterUserUseCase(repo, log);
        }
}
