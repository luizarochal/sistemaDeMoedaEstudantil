package sistema.estudantil.system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Mérito Estudantil")
                        .version("1.0")
                        .description("Sistema para gerenciamento de moedas de mérito entre professores, alunos e empresas parceiras")
                        .contact(new Contact()
                                .name("Suporte")
                                .email("suporte@sistemamerito.com")));
    }
}