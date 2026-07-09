package com.buxbuddy.auth.security;

import com.buxbuddy.auth.config.CustomUserDetailsService;
import com.buxbuddy.auth.dto.api.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.time.LocalDateTime;
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getServletPath();
        // Public APIs - No JWT required
        if (path.equals("/api/auth/register") ||
                path.equals("/api/auth/login")) {

            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        // No JWT token present - continue request
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7);
        try {
            // Validate JWT token
            if (!jwtService.validateToken(token)) {
                sendErrorResponse(
                        response,
                        request,
                        "JWT token is invalid or expired"
                );
                return;
            }
            String username = jwtService.extractUsername(token);
            if (username != null &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication() == null) {


                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);


                System.out.println(
                        "USER EMAIL : "
                                + userDetails.getUsername()
                );
                System.out.println(
                        "AUTHORITIES : "
                                + userDetails.getAuthorities()
                );
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authenticationToken);

            }
        } catch (Exception e) {
            sendErrorResponse(
                    response,
                    request,
                    "JWT token is invalid or expired"
            );

            return;
        }
        System.out.println(
                "GOING TO CONTROLLER : "
                        + request.getRequestURI()
        );
        filterChain.doFilter(request, response);

    }

    private void sendErrorResponse(
            @NonNull HttpServletResponse response,
            @NonNull HttpServletRequest request,
            @NonNull String message
    ) throws IOException {

        ApiErrorResponse errorResponse =
                ApiErrorResponse.builder()
                        .status(HttpServletResponse.SC_UNAUTHORIZED)
                        .error("Unauthorized")
                        .message(message)
                        .path(request.getRequestURI())
                        .timestamp(LocalDateTime.now())
                        .build();
        response.setStatus(
                HttpServletResponse.SC_UNAUTHORIZED
        );
        response.setContentType(
                "application/json"
        );
        response.getWriter()
                .write(
                        objectMapper.writeValueAsString(errorResponse)
                );
    }
}