package spring.gr.socioai.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import spring.gr.socioai.security.exceptions.AccessDeniedExceptionHandler;
import spring.gr.socioai.security.exceptions.AuthenticationEntryPointExceptionHandler;
import spring.gr.socioai.security.filters.JwtFilter;
import spring.gr.socioai.model.valueobjects.UserRole;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfigurations {

    private final JwtFilter jwtFilter;

    public SecurityConfigurations(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Value("${app.security.origins-allowed}")
    String originsAllowed;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(this.corsConfiguration()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new AuthenticationEntryPointExceptionHandler())
                        .accessDeniedHandler(new AccessDeniedExceptionHandler()))

                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                        .requestMatchers("/api/**").hasAnyRole(UserRole.USER.getRole().toUpperCase(), UserRole.ADMIN.getRole().toUpperCase())
                        .anyRequest().authenticated())

                // Não tem problema por antes de um filtro desativado
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    private CorsConfigurationSource corsConfiguration() {
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.setMaxAge(3600L);
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Origin"));
        cors.setAllowedOrigins(List.of(originsAllowed));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return source;
    }
}

