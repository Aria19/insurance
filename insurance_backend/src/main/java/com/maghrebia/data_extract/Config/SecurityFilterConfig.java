package com.maghrebia.data_extract.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.maghrebia.data_extract.Business.Services.JwtService;

@Configuration
public class SecurityFilterConfig {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public SecurityFilterConfig(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }
}
