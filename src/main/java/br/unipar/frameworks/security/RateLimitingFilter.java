package br.unipar.frameworks.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Order(1)
public class RateLimitingFilter extends OncePerRequestFilter {

    private final Map<String, RequestWindow> windows = new ConcurrentHashMap<>();
    private final int maxRequests;
    private final long windowSeconds;

    public RateLimitingFilter(
            @Value("${security.rate-limit.max-requests:120}") int maxRequests,
            @Value("${security.rate-limit.window-seconds:60}") long windowSeconds
    ) {
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String key = request.getRemoteAddr();
        long currentWindow = Instant.now().getEpochSecond() / windowSeconds;
        RequestWindow window = windows.compute(key, (ignored, existing) -> {
            if (existing == null || existing.window() != currentWindow) {
                return new RequestWindow(currentWindow, new AtomicInteger(1));
            }
            existing.requests().incrementAndGet();
            return existing;
        });

        if (window.requests().get() > maxRequests) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private record RequestWindow(long window, AtomicInteger requests) {
    }
}
