package com.ayush.blogproject.config;

import com.ayush.blogproject.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/api/**")
                )
                .userDetailsService(userDetailsService)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/blogs").permitAll()
                        //  allow — read, filter, search, comment
                        .requestMatchers(HttpMethod.GET, "/", "/blog", "/updateblog").permitAll()
                        .requestMatchers(HttpMethod.POST, "/addcomment").permitAll()
                        .requestMatchers("/login", "/register").permitAll()

                        // AUTHOR aur ADMIN — post create/edit/delete
                        .requestMatchers("/createposts", "/blogs", "/updatepost",
                                "/updatepublishblog", "/deletepost")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_AUTHOR")

                        // logged in — comment edit/delete
                        .requestMatchers("/editcomments", "/deletecomment")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_AUTHOR")

                        .anyRequest().authenticated()

//                .requestMatchers("/api/auth/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/posts/**/comments").permitAll()
//                .requestMatchers("/api/posts/**", "/api/comments/**")
//                .hasAnyAuthority("ROLE_ADMIN", "ROLE_AUTHOR")
//
//                .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())   // <-- add this for API authentication
                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/blog", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/blog")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}