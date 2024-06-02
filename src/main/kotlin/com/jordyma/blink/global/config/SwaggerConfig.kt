package com.jordyma.blink.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {

    @Bean
    fun swaggerApi(): Docket = Docket(DocumentationType.OAS_30)
//        .consumes(getConsumeContentTypes())
//        .produces(getProduceContentTypes())
//        .apiInfo(swaggerInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.jordyma.blink/"))
        .paths(PathSelectors.any())
        .build()
//        .useDefaultResponseMessages(false)

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

//    @Bean
//    fun api(): Docket {
//        return Docket(DocumentationType.OAS_30)
//            .select()
//            .apis(RequestHandlerSelectors.basePackage("com.example.demo.controller"))
//            .paths(PathSelectors.any())
//            .build()
//            .apiInfo(apiInfo())
//    }
//
//    private fun apiInfo(): ApiInfo {
//        return ApiInfoBuilder()
//            .title("Demo API")
//            .description("Demo API for showing Swagger documentation")
//            .version("1.0.0")
//            .build()
//    }
}