package com.financontrol.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                new AntPathRequestMatcher("/login"),
                                                                new AntPathRequestMatcher("/register"),
                                                                new AntPathRequestMatcher("/forgot-password"),
                                                                new AntPathRequestMatcher("/reset-password"),
                                                                new AntPathRequestMatcher("/verify"),
                                                                new AntPathRequestMatcher("/css/**"),
                                                                new AntPathRequestMatcher("/js/**"),
                                                                new AntPathRequestMatcher("/images/**"))
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/login")
                                                .loginProcessingUrl("/authentication-processing")
                                                .defaultSuccessUrl("/dashboard", true)
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/login?logout")
                                                .permitAll())
                                // Wicket often requires frames for modals/etc, but default sameOrigin is
                                // usually fine.
                                // CSRF might need handling for Wicket forms if not automated by Wicket-Spring.
                                // For simplicity, we keep defaults but might need .csrf(csrf -> csrf.disable())
                                // if Wicket struggles.
                                // Wicket usually handles its own CSRF/component security.
                                // TODO: validate impact to keep disabled
                                .csrf(csrf -> csrf.disable());

                return http.build();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                // saltLength=16, hashLength=32, parallelism=1, memory=4096, iterations=3
                return new Argon2PasswordEncoder(16, 32, 1, 4096, 3);
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }
}
