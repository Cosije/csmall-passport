package cn.tedu.csmall.passport.config;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@MapperScan("cn.tedu.csmall.passport.mapper")
public class MybatisConfiguration {
    public MybatisConfiguration() {
        log.info("创建配置类：MybatisConfiguration");
    }

}
