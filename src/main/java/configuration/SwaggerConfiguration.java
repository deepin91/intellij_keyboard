package configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("controller"))
                .paths(PathSelectors.any())
                .build();
    }

//    private ApiInfo apiInfo(){
//        return new ApiInfoBuilder()
//                .title("Spring Boot Open API with Swagger")
//                .description("noName REST API")
//                .version("1.0.0") //<--버전 확인 및 수정 필요
//                .build();
//    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Spring Boot Open API Documentation")
                .description("API documentation for Example Project")
                .version("1.0.0")  // 버전 정보 수정 가능
                .build();
    }
}
