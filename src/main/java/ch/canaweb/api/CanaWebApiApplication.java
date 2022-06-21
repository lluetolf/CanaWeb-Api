package ch.canaweb.api;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CanaWebApiApplication {


    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CanaWebApiApplication.class, args);

        String firestoreCredentialsLocation = ctx.getEnvironment().getProperty("spring.cloud.gcp.firestore.project-id");
        System.out.println("GCP Config:" + firestoreCredentialsLocation);
    }

}
