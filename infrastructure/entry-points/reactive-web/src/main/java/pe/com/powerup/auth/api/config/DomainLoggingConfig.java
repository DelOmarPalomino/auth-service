package pe.com.powerup.auth.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pe.com.powerup.auth.model.common.DomainLogger;

@Configuration
public class DomainLoggingConfig {

    @Bean
    public DomainLogger domainLogger() {
        Logger logger = LoggerFactory.getLogger("domain");
        return new DomainLogger() {
            @Override
            public void debug(String msg, Object... args) {
                logger.debug(msg, args);
            }

            @Override
            public void info(String msg, Object... args) {
                logger.info(msg, args);
            }

            @Override
            public void warn(String msg, Object... args) {
                logger.warn(msg, args);
            }

            @Override
            public void error(String msg, Object... args) {
                logger.error(msg, args);
            }
        };
    }
}
