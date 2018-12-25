package com.yzjiang.web.kaptcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 验证码配置
 */
@Configuration
public class KaptchaConfig {

    @Bean({"kaptchaProperties"})
    @ConfigurationProperties(prefix = "kaptcha")
    public Properties getProperties() {
        return new Properties() {
            private final String PREFIX = "kaptcha";

            @Override
            public synchronized Object put(Object key, Object value) {
                return super.put(PREFIX + "." + key, value);
            }
        };
    }

    @Autowired
    @Bean({"kaptchaProducer"})
    public Producer getProducer(
            @Qualifier("kaptchaProperties") Properties kaptchaProperties) {
        Config config = new Config(kaptchaProperties);
        return config.getProducerImpl();
    }
}