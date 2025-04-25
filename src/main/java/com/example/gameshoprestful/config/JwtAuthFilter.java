package com.example.gameshoprestful.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.gameshoprestful.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Value("${jwt.static-token}")
    private String staticToken;

    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        System.out.println("Authorization header: " + header);

        String subject = null;
        String jwt = null;
        boolean isStaticToken = false;

        if (header != null && header.startsWith("Bearer ")) {
            jwt = header.substring(7);
            // 检查是否是静态 Token
            if (jwt.equals(staticToken)) {
                isStaticToken = true;
                try {
                    subject = jwtUtil.extractSubject(jwt);
                    System.out.println("Static token detected, extracted subject: " + subject);
                } catch (RuntimeException e) {
                    System.err.println("Failed to extract subject from static token: " + e.getMessage());
                }
            } else {
                // 动态 Token 验证
                try {
                    subject = jwtUtil.extractSubject(jwt);
                    System.out.println("Extracted subject: " + subject);
                } catch (RuntimeException e) {
                    System.err.println("Failed to extract subject: " + e.getMessage());
                }
            }
        } else {
            System.out.println("No valid Authorization header found");
        }

        if (subject != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                boolean isValid;
                if (isStaticToken) {
                    // 静态 Token 已在上一步验证，这里直接通过
                    isValid = true;
                } else {
                    // 动态 Token 验证
                    isValid = jwtUtil.validateToken(jwt, subject);
                }

                if (isValid) {
                    // 为静态 Token 或特定用户分配权限
                    List<SimpleGrantedAuthority> authorities = subject.equals("test") // 静态 Token 的 subject 是 "test"
                            ? List.of(new SimpleGrantedAuthority("ROLE_TEST"))
                            : List.of(new SimpleGrantedAuthority("ROLE_USER")); // 普通用户暂无角色

                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            subject, null, authorities);
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("Authentication set for subject: " + subject + " with authorities: " + authorities);
                } else {
                    System.err.println("JWT validation failed for subject: " + subject);
                }
            } catch (RuntimeException e) {
                System.err.println("JWT validation error: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}