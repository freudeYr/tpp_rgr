package tpp.city.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import tpp.city.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    public SecurityConfig(UserService userDetailsService) {
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/", "/login", "/error").permitAll()
                .requestMatchers("/index").authenticated()
                .requestMatchers("/city/**", "/street/**", "/house/**", "/apartment/**").hasRole("ADMIN")  // Доступ тільки для Admin
                .anyRequest().authenticated())  // Всі інші запити потребують авторизації
            .formLogin(login -> login
                .loginPage("/login") // Сторінка для входу
                .defaultSuccessUrl("/index", true) // Після успішного входу перенаправляти на /index
                .permitAll())
            .logout(logout -> logout
                .permitAll());

        return http.build();
    }

        @SuppressWarnings("deprecation")
        @Bean
    public NoOpPasswordEncoder passwordEncoder() {
        return (NoOpPasswordEncoder) NoOpPasswordEncoder.getInstance(); // Вимкнути кодування паролів
    }
}