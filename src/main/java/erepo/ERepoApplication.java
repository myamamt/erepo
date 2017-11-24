package erepo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@EnableAutoConfiguration
public class ERepoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ERepoApplication.class, args);
    }
}
