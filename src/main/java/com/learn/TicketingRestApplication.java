package com.learn;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketingRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketingRestApplication.class, args);
    }

    @Bean
    public ModelMapper mapper(){

        return new ModelMapper();
    }
}
