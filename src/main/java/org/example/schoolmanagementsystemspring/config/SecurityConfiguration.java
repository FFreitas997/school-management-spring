package org.example.schoolmanagementsystemspring.config;

import lombok.RequiredArgsConstructor;
import org.example.schoolmanagementsystemspring.authentication.service.AuthenticationFilterService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.example.schoolmanagementsystemspring.user.entity.Permission.*;
import static org.example.schoolmanagementsystemspring.user.entity.Role.ADMIN;
import static org.springframework.http.HttpMethod.*;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private static final String[] PERMIT_ALL_URLS = {
            "/resources/**",
            "/css/**",
            "/images/**",
            "/api/v1/auth/**",
            "/api/v1/admin/management-server",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };

    private final AuthenticationFilterService authenticationFilterService;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String actuatorURL = "/api/v1/admin/management-server/**";
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF because we are using JWT authentication
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PERMIT_ALL_URLS).permitAll();
                    auth.requestMatchers(actuatorURL).hasRole(ADMIN.name());
                    auth.requestMatchers(GET, actuatorURL).hasAuthority(ADMIN_READ.name());
                    auth.requestMatchers(POST, actuatorURL).hasAuthority(ADMIN_CREATE.name());
                    auth.requestMatchers(PUT, actuatorURL).hasAuthority(ADMIN_UPDATE.name());
                    auth.requestMatchers(DELETE, actuatorURL).hasAuthority(ADMIN_DELETE.name());
                    auth.anyRequest().authenticated();
                })
                .sessionManagement(param -> param.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authenticationFilterService, UsernamePasswordAuthenticationFilter.class)
                .logout(param -> {
                    param.logoutUrl("/api/v1/auth/logout");
                    param.addLogoutHandler(logoutHandler);
                    param.logoutSuccessHandler((req, res, auth) -> SecurityContextHolder.clearContext());
                })
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("*"));
        config.setAllowedOrigins(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
