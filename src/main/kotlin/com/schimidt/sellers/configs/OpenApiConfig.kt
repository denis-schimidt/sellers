package com.schimidt.sellers.configs

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI {
        return OpenAPI().apply {
            info(
                Info()
                    .title("Sellers API")
                    .description("API to manage sellers example")
                    .summary("API to manage sellers")
                    .version("1.0.0")
            )
        }
    }
}
