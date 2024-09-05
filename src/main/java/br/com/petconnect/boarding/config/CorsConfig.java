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

        // Permitir que cookies e credenciais sejam enviados
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("https://app.kinvo.com.br");

        // Permitir todas as origens (atenção: isso é menos seguro, use com cautela em produção)
        config.addAllowedOriginPattern("*");

        // Permitir todos os cabeçalhos
        config.addAllowedHeader("*");

        // Permitir métodos como GET, POST, PUT, DELETE, OPTIONS
        config.addAllowedMethod("*");

        // Cabeçalhos específicos que você deseja garantir que estejam presentes
        config.addAllowedHeader("Authorization");
        config.addAllowedHeader("Content-Type");

        // Expor o cabeçalho de autorização
        config.addExposedHeader("Authorization");

        // Fonte de configuração do CORS
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // Aplicar a todas as rotas

        return new CorsFilter(source);
    }
}