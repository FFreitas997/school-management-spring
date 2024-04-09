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

import static org.example.schoolmanagementsystemspring.user.entity.Permission.ADMIN_READ;
import static org.example.schoolmanagementsystemspring.user.entity.Role.ADMIN;
import static org.springframework.http.HttpMethod.GET;

/**
 * @author FFreitas
 * @LinkedIn: <a href="https://www.linkedin.com/in/francisco-freitas-a289b91b3/">Francisco Freitas</a>
 * @Github: <a href="https://github.com/FFreitas997">FFreitas997</a>
 * @Project: School-Management-System-Spring
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private static final String[] PERMIT_ALL_URLS = {
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
        return http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF because we are using JWT authentication
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(PERMIT_ALL_URLS).permitAll();
                    auth.requestMatchers("/api/v1/admin/management-server/**").hasRole(ADMIN.name());
                    auth.requestMatchers(GET,"/api/v1/admin/management-server/**").hasAuthority(ADMIN_READ.name());
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
}
