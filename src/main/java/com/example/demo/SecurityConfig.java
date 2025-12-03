package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(req -> req
            .requestMatchers("/login.html", "/auth/login",
                             "/form.css", "/", "/home",
                             "/index.html", "/ai/generate", "/trips")
            .permitAll()
            .anyRequest().authenticated()
        )
        .formLogin(form -> form.disable())
        .logout(logout -> logout
            .logoutUrl("/auth/logout")          // Spring Security logout URL
            .logoutSuccessUrl("/login.html")    // where to go after logout
            .permitAll()
        )
        .csrf(csrf -> csrf.disable());

    return http.build();
}

//         @Bean
//         public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//                 .authorizeHttpRequests(requests -> requests
//                 .requestMatchers("/login.html", "/form.css",
//                              "/", "/auth/login", "/home",
//                              "/index.html", "/ai/generate", "/trips").permitAll()
//                 .anyRequest().authenticated()
//                 )
//                 .formLogin(form -> form
//                         .loginPage("/login")
//                         .permitAll()
//                 )
//                 .logout(logout -> logout.permitAll())
//                 .csrf(csrf -> csrf.disable());  // correct syntax

//         return http.build();
// }


    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("USER")
                        .build();

        return new InMemoryUserDetailsManager(user);
    }
}
