package com.lmm;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * 版权声明：Copyright(c) 2019
 *
 * @program: lmmvideos
 * @Author minmin.liu
 * @Date 2019-02-25 14:42
 * @Version 1.0
 * @Description j继承SpringBootServletInitializer,相当于使用web.xml的形式去启动部署
 */
public class WarStartApplication extends SpringBootServletInitializer {
    /**
     * 重写配置--》configure
     * @param builder
     * @return
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //使用web.xml运行应用程序，指向Application.最后启动springboot
        return builder.sources(Application.class);
    }
}
