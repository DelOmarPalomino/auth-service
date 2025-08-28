package pe.com.powerup.auth.model.common;

public interface DomainLogger {
    void debug(String msg, Object... args);

    void info(String msg, Object... args);

    default void warn(String msg, Object... args) {
    }

    default void error(String msg, Object... args) {
    }
}
