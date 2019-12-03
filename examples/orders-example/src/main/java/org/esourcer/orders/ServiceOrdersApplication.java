package org.esourcer.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = { "org.esourcer" })
@EntityScan(basePackages = { "org.esourcer.jpa" })
public class ServiceOrdersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceOrdersApplication.class, args);
    }

}
