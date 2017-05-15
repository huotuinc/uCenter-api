package com.huotu.ucphp.config;

import com.huotu.ucphp.service.UCUserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 可以提供uCenter服务端功能，访问入口是 /uCenter
 * 在载入该配置前，依赖spring服务{@link UCUserService}
 *
 * Created by xyr on 2017/4/25.
 */
@Configuration
//@EnableTransactionManagement(mode = AdviceMode.PROXY)
//@EnableAspectJAutoProxy
@ComponentScan({
        "com.huotu.ucphp.controller",
        "com.huotu.ucphp.service"
})
//@EnableJpaRepositories("com.huotu.huotao.repository")
//@EnableScheduling
public class UCenterConfig {
}
