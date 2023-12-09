package com.hodik.elastic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .securityMatcher("/*").authorizeHttpRequests(authorize -> authorize
//                        .anyRequest().permitAll());
////                .sessionManagement(sessionManagement ->
////                        sessionManagement
////                                .sessionConcurrency(sessionConcurrency -> sessionConcurrency.maximumSessions(1))
////                                .sessionCreationPolicy(STATELESS))
////                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.anyRequest().authenticated());
////        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
//        return http.build();
        http
                .csrf(AbstractHttpConfigurer::disable)
//                .securityMatcher(HttpMethod.GET, "/*").authorizeHttpRequests(authorize ->
//                        authorize
//                        .anyRequest().hasAnyRole(Role.ROLE_USER.name(), Role.ROLE_ADMIN.name(), Role.ROLE_APP.name()))
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionConcurrency(sessionConcurrency -> sessionConcurrency.maximumSessions(1))
                                .sessionCreationPolicy(STATELESS))
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests.anyRequest().authenticated());
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
