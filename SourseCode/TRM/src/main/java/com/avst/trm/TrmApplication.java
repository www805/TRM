package com.avst.trm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan({"com.avst.trm.v1.common.datasourse.base.mapper","com.avst.trm.v1.common.datasourse.police.mapper"})
@EnableScheduling
public class TrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrmApplication.class, args);
    }

}
