package com.yzjiang.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 *
 *
 * @author Administrator
 */
// @SpringBootApplication(scanBasePackages = "com.learn.yzjiang")  //自定义扫描代码包路径
// @EnableDubboConfiguration  //使用dubbo配置
@SpringBootApplication(scanBasePackages = "com.yzjiang")
@EnableJpaRepositories(basePackages = "com.yzjiang.dao")
@EntityScan("com.yzjiang.dao")
public class YzjiangApplication {

    public static void main(String[] args) {
        SpringApplication.run(YzjiangApplication.class, args);
    }
}
