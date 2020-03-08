package com.netposa.vetl;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.netposa.vetl.dao")
public class VetlApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetlApplication.class, args);
    }

}
