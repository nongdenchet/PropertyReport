package rain.property.report;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@EnableMongoAuditing
@SpringBootApplication
public class PropertyReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyReportApplication.class, args);
    }
}
