package com.huotu.ucphp.controller;

import com.huotu.ucphp.BaseTest;
import com.huotu.ucphp.util.AuthCodeUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

/**
 * Created by xyr on 2017/5/2.
 */
public class UCControllerTest extends BaseTest {

    @Autowired
    private Environment environment;

    @Test
    public void synlogin() throws Exception {

        //System.out.println(environment.getProperty("testName"));

        //content时间过时
        mockMvc.perform(MockMvcRequestBuilders.post("/uCenter/index.php?__times__=1")
                .contentType("application/x-www-form-urlencoded")
                .content("m=user&a=logincheck&inajax=2&release=20170101&input=48dem81vMNs6UFsfuZZ3Zxv1S%2BBOY%2FXT%" +
                        "2Fm6y%2BidrTYtedrUZki0WH1wsr1L5N5%2B7ubNupPu1r5UkV7rsM9RZVccNt6wMvD9arG6BxLmi2ZpSw5YYsjIHVw" +
                        "PLfNDktin0Wk15gnQJ%2Fp%2BP99zXYX7J31ikADO9&appid=1")
        )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.cookie().doesNotExist("loginuser"));

        //当前登录
        Date now = new Date();
        String test = "username=testUser&ip=112.16.89.50&agent=be0ff8791ae794e1841efdd14b50475f&time=" + now.getTime();
        String key = "testKeyJustForForever";
        AuthCodeUtil util = new AuthCodeUtil();
        String afStr = util.authcodeEncode(test, key);
        mockMvc.perform(MockMvcRequestBuilders.post("/uCenter/index.php?__times__=1")
                        .contentType("application/x-www-form-urlencoded")
                        .param("m", "user")
                        .param("a", "logincheck")
                        .param("inajax", "2")
                        .param("release", "20170101")
                        .param("input", afStr)
                        .param("appid", "1")
        )
                .andExpect(MockMvcResultMatchers.cookie().exists("loginuser"));

    }

    @Test
    public void synlogout() throws Exception {

    }

}
