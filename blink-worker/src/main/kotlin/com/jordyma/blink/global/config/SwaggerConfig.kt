package com.jordyma.blink.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.*
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spi.service.contexts.SecurityContext
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig() {

    @Bean
    fun swaggerApi(): Docket = Docket(DocumentationType.OAS_30)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.jordyma.blink"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(swaggerInfo())
        .securitySchemes(listOf(apiKey()))
        .securityContexts(listOf(securityContext()))


    private fun swaggerInfo() = ApiInfoBuilder()
        .title("Blink API")
        .description("Blink API Documentation")
        .version("1.0.0")
        .build()

    private fun getConsumeContentTypes(): Set<String> {
        val consumes = HashSet<String>()
        consumes.add("multipart/form-data")
        return consumes
    }

    private fun getProduceContentTypes(): Set<String> {
        val produces = HashSet<String>()
        produces.add("application/json;charset=UTF-8")
        return produces
    }

    private fun securityContext(): SecurityContext = SecurityContext.builder()
        .securityReferences(defaultAuth())
        .build()

    private fun defaultAuth(): List<SecurityReference> {
        val authorizationScope = AuthorizationScope("global", "accessEverything")
        val authorizationScopes = arrayOf(authorizationScope)
        return listOf(SecurityReference("JWT", authorizationScopes))
    }

    private fun apiKey(): ApiKey = ApiKey("JWT", "Authorization", "header")

}