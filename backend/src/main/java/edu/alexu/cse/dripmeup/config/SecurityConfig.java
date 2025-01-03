package edu.alexu.cse.dripmeup.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import edu.alexu.cse.dripmeup.JwtAuthFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter authFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Disable CSRF for stateless APIs
                .cors(cors -> cors
                        .configurationSource(request -> {
                            var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                            corsConfiguration.setAllowedOrigins(List.of("http://localhost:8080")); // Add your frontend
                                                                                                   // URL here
                            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                            corsConfiguration.setAllowedHeaders(List.of("*"));
                            return corsConfiguration;
                        }))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/5/users/login",
                                "/api/5/users/signup",
                                "/api/5/users/g/login",
                                "/api/5/users/g/signup",
                                "/api/5/users/getUsername",
                                "/api/5/users/signup/code",
                                "/api/5/users/forgotPassword/code",
                                "/api/5/users/checkCode",
                                "/api/5/users/signup",
                                "/api/5/users/signup",
                                "/api/6/admin/login",
                                "/api/6/admin/signup",
                                "/api/7/categories/",
                                "/api/feedback/product/{ProductID}",
                                "/api/feedback/**",
                                "/api/feedback",
                                "/api/feedback/user/{UserID}",
                                "/api/7/categories/{categoryName}",
                                "/users/{UserID}",
                                "/api/1000/shop/c/product",
                                "/api/1000/shop/c/image",
                                "/api/1000/shop/c/variant",
                                "/api/1000/shop/products",
                                "/orders/create-order"


                        ).permitAll()
                        .anyRequest().authenticated() // Protect all other endpoints
                )
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // No sessions
                )
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Password encoding
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}