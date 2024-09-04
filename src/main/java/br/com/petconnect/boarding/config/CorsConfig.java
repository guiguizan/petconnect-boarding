package br.com.petconnect.boarding.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Permitir credenciais (cookies, auth headers)
        config.addAllowedOriginPattern("*"); // Permitir todas as origens
        config.addAllowedHeader("*"); // Permitir todos os headers
        config.addAllowedMethod("*"); // Permitir todos os métodos (GET, POST, etc.)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplicar a todas as rotas

        return new CorsFilter(source);
    }
}