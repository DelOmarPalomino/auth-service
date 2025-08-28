package pe.com.powerup.auth.api.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter implements WebFilter {

        private static final String CID = "cid";

        @Override
        public Mono<Void> filter(ServerWebExchange ex, WebFilterChain chain) {
                String cid = Optional.ofNullable(ex.getRequest().getHeaders().getFirst("X-Correlation-Id"))
                                .orElse(UUID.randomUUID().toString());
                long t0 = System.currentTimeMillis();

                MDC.put(CID, cid);
                log.info("REQ {} {} from {}", ex.getRequest().getMethod(), ex.getRequest().getURI().getPath(),
                                ex.getRequest().getRemoteAddress());

                return chain.filter(ex)
                                .doOnError(err -> log.error("ERR {} {} -> {}", ex.getRequest().getMethod(),
                                                ex.getRequest().getPath().value(), err.toString()))
                                .doFinally(sig -> {
                                        // HttpStatusCode (Spring 6) -> int
                                        int code = Optional.ofNullable(ex.getResponse().getStatusCode())
                                                        .map(HttpStatusCode::value)
                                                        .orElse(HttpStatus.OK.value());
                                        log.info("RES {} {} -> {} ({} ms)",
                                                        ex.getRequest().getMethod(),
                                                        ex.getRequest().getPath().value(),
                                                        code,
                                                        (System.currentTimeMillis() - t0));
                                        MDC.remove(CID);
                                });
        }
}
