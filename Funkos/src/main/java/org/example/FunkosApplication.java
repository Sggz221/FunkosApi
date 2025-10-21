package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@Slf4j
@SpringBootApplication
public class FunkosApplication {

    static void main(String[] args) {
		SpringApplication.run(FunkosApplication.class, args);
	}

}
