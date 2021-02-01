package pl.coderslab.cls_wms_app.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FeaturesApplication {
    public static void main(String[] args) {
        SpringApplication.run(FeaturesApplication.class, args);

    }
}
