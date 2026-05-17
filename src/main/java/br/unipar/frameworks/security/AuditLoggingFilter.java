package br.unipar.frameworks.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(2)
public class AuditLoggingFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuditLoggingFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        filterChain.doFilter(request, response);

        if (isSensitivePath(request.getRequestURI()) || response.getStatus() >= 400) {
            LOGGER.info(
                    "audit method={} path={} status={} remote={}",
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    request.getRemoteAddr()
            );
        }
    }

    private boolean isSensitivePath(String path) {
        return path.startsWith("/api/auth")
                || path.startsWith("/api/admin")
                || path.startsWith("/api/users")
                || path.startsWith("/api/debug")
                || path.startsWith("/h2-console");
    }
}
