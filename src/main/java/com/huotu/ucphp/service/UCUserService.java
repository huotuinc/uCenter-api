package com.huotu.ucphp.service;

/**
 * Created by xyr on 2017/5/2.
 */
public interface UCUserService {

    /**
     * 校验一个用户
     * @param username 登录名
     * @param password ？？
     * @return true 表示用户校验通过
     */
    boolean validUser(String username, String password);

}
