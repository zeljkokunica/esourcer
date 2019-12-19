package org.esourcer.orders.config.security;

import lombok.extern.slf4j.Slf4j;
import org.esourcer.orders.common.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class SimpleApiKeyFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain)
            throws ServletException, IOException {
        final String apiKey = request.getHeader("x-api-key");
        if (StringUtils.isEmpty(Optional.ofNullable(apiKey).orElse("").trim())) {
            chain.doFilter(request, response);
            return;
        }
        final String[] parts = apiKey.split(";");
        final String userId = parts[0];
        final String permissions = parts.length > 1 ? parts[1] : null;
        final User user = new User(userId, permissions);
        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getId(),
                "",
                user.permissions().stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

}
