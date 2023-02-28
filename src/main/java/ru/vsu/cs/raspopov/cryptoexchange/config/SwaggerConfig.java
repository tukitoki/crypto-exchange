package ru.vsu.cs.raspopov.cryptoexchange.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.regex("/api/.*"))
                .build()
                .apiInfo(apiDetail());
    }

    private ApiInfo apiDetail() {
        return new ApiInfo(
                "Crypto exchange API",
                "FULL API documentation here",
                "v1.0",
                "Full free to use",
                "tukitoki",
                "API license",
                "url"
        );
    }
}
