package org.test.order.infra.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer customOpenApi() {
        return openApi -> openApi.info(new Info()
                .title("MS Order API")
                .description("Order API")
                .version("1.0.0")
                .termsOfService(""));

    }
}
