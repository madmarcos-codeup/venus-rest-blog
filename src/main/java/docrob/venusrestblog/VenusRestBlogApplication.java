package docrob.venusrestblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = "docrob.venusrestblog.controller")
public class VenusRestBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(VenusRestBlogApplication.class, args);
    }

}
