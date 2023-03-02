package awslee.v1.springawslee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpringAwsLeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringAwsLeeApplication.class, args);
    }

}
