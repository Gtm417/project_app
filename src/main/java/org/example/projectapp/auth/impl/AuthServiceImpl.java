package org.example.projectapp.auth.impl;

import org.example.projectapp.auth.AuthService;
import org.example.projectapp.auth.exception.UserNotFoundException;
import org.example.projectapp.auth.jwt.JwtTokenProvider;
import org.example.projectapp.model.User;
import org.example.projectapp.repository.UserRepository;
import org.example.projectapp.security.SecurityUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public AuthServiceImpl(JwtTokenProvider jwtTokenProvider,
                           @Qualifier("userDetailsServiceImpl") UserDetailsService userDetailsService, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    public User getUserFromAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUser(authentication);
    }

    @Override
    public User getUserFromAuth(Authentication authentication) {
        return getUser(authentication);
    }

    private User getUser(Authentication authentication) {
        if (authentication == null) {
            logger.info("[AUTH] Authentication is null");
            throw new AuthenticationServiceException("Authentication is null");
        }

        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (securityUser == null) {
            logger.info("[AUTH] User principals {} is not found", authentication.getName());
            throw new AuthenticationServiceException("Principal is not found");
        }

        String email = securityUser.getUsername();
        if (!authentication.isAuthenticated()) {
            logger.info("[AUTH] User {} is not authenticated", email);
            throw new AuthenticationServiceException("User is not is not authenticated");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found", email));
    }

    @Override
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(jwtTokenProvider.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }}