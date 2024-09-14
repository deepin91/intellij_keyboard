package configuration;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public Docker api(){
        return new Docker(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("noName"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Spring Boot Open API with Swagger")
                .description("noNameAPI")
                .version("1.0.0") //<--버전 확인 및 수정 필요
                .build();
    }
}
