package com.arifrajib.springsecurityjwt.secutiry;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private UserDetailsService userDetailsService;
    private JwtUtilityService jwtUtilityService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")) {

            String jwt = authHeader.substring(7);
            if (jwt == null || jwt.isBlank()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token in Bearer header");
            }
            else {
                try {
                    String email = jwtUtilityService.validateTokenAndRetrieveSubject(jwt);
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                    Authentication authToken = new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());

                    SecurityContext context = SecurityContextHolder.getContext();
                    if (context.getAuthentication() == null) {
                        context.setAuthentication(authToken);
                    }
                }
                catch (JWTVerificationException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT token");
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
