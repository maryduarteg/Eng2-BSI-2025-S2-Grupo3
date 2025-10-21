package com.example.proj_bga;

import com.example.proj_bga.util.SingletonDB;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjetoBgaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoBgaApplication.class, args);

        SingletonDB.conectar();
    }
}
