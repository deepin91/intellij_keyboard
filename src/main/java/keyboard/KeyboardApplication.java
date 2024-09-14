package keyboard;
@SpringBootApplication
public class KeyboardApplication {
    public static void main(String[] args){
        SpringApplication.run(KeyBoardApplication.class, args);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
