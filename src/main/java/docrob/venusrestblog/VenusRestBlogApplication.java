package docrob.venusrestblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
/*
Since Spring Boot 1.2.0, we can use the @SpringBootApplication annotation, which is a combination of the three annotations @Configuration, @EnableAutoConfiguration, and@ComponentScan with their default attributes.
so... we don't seem to need ComponentScan
may still need something for repositories
 */
//@ComponentScan(basePackages = "docrob.venusrestblog")
public class VenusRestBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenusRestBlogApplication.class, args);
    }

}
