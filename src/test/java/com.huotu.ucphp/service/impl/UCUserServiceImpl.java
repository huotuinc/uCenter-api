package com.huotu.ucphp.service.impl;

import com.huotu.ucphp.service.UCUserService;
import org.springframework.stereotype.Service;

/**
 * Created by xyr on 2017/5/2.
 */
@Service
public class UCUserServiceImpl implements UCUserService {

    public boolean validUser(String username, String password) {
        return true;
    }

    @Override
    public boolean userCheck(String username) {
        return true;
    }
}
