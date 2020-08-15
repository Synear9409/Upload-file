package com.synear.file.uploadfile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.synear.file.uploadfile.dao")
public class UploadFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(UploadFileApplication.class, args);
    }

}
