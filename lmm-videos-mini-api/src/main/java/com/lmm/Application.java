/**
 * @program: lmmvideos
 * @description: 启动类
 * @author: minmin.liu
 * @create: 2018-09-26 16:17
 **/
package com.lmm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.lmm.mapper")
@ComponentScan(basePackages = {"com.lmm","org.n3r.idworker"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
