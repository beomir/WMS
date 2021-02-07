package pl.coderslab.cls_wms_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ClsWmsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(ClsWmsAppApplication.class, args);

    }

}
