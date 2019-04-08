package com.avst.trm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan({"com.avst.trm.v1.common.datasourse.base.mapper","com.avst.trm.v1.common.datasourse.police.mapper"})
public class TrmApplication {

    public static void main(String[] args) {
        SpringApplication.run(TrmApplication.class, args);
    }

}
