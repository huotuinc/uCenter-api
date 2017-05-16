package com.huotu.ucphp.controller;

import com.huotu.ucphp.service.UCUserService;
import com.huotu.ucphp.util.AuthCodeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyr on 2017/4/25.
 */
@Controller
public class UCController {

    private static final Log log = LogFactory.getLog(UCController.class);
    public static boolean API_SYNLOGIN=true;		//note 同步登录 API 接口开关
    public static boolean API_SYNLOGOUT=true;		//note 同步登出 API 接口开关

    public static String API_RETURN_SUCCEED   =    "1";
    public static String API_RETURN_FAILED    =   "-1";
    public static String API_RETURN_FORBIDDEN =   "-2";

    @Autowired
    private UCUserService ucUserService;
    @Autowired
    private Environment environment;

    /**
     * 同步登录
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/uCenter/**")
    public void doRequest(@RequestParam("a") String requestMapping,@RequestParam("input") String code, HttpServletRequest request
            , HttpServletResponse response) throws IOException {
        log.info("RequestContent: " + requestMapping);

        Map<String, String> get = new HashMap<>();
        String key = environment.getProperty("uCenterKey");
        code = AuthCodeUtil.authcodeDecode(code, key);
        log.info("afterDecode: " + code);
        parse_str(code, get);

        if (get.isEmpty()) {
            response.getWriter().print("Invalid Request");
            return;
        }
        if(time() - tolong(get.get("time")) > 3600) {
            response.getWriter().print("Authracation has expiried");
            return;
        }
        if ("logincheck".equals(requestMapping)) { //同步登录

            if(!API_SYNLOGIN ) {
                response.getWriter().print(API_RETURN_FORBIDDEN);
                return;
            }

            String username = get.get("username");
            if (!ucUserService.userCheck(username)) {
                response.getWriter().print(API_RETURN_FAILED);
                return;
            }

            //同步登录 API 接口
            response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

            int cookietime = 31536000;

            Cookie user = new Cookie("loginuser",get.get("username"));
            user.setMaxAge(cookietime);
            response.addCookie(user);
        } else if ("userlogin".equals(requestMapping)) { //登录验证

            String username = get.get("username");
            String password = get.get("password");
            //还不太清楚具体流程，暂时那么写
            if (!ucUserService.validUser(username, password)) {
                response.getWriter().print(API_RETURN_FAILED);
                return;
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                return;
            }

        } else{ //同步登出
            if(!API_SYNLOGOUT ) {
                response.getWriter().print(API_RETURN_FORBIDDEN);
                return;
            }

            //同步登出 API 接口
            response.addHeader("P3P"," CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV" +
                    " OTC NOI DSP COR\"");

            Cookie user = new Cookie("loginuser","");
            user.setMaxAge(0);
            response.addCookie(user);
        }
    }

    private void parse_str(String str, Map<String,String> sets){
        if(str==null||str.length()<1)
            return;
        String[] ps = str.split("&");
        for(int i=0;i<ps.length;i++){
            String[] items = ps[i].split("=");
            if(items.length==2){
                sets.put(items[0], items[1]);
            }else if(items.length ==1){
                sets.put(items[0], "");
            }
        }
    }
    private long time(){
        return System.currentTimeMillis()/1000;
    }

    private static long tolong(Object s){
        if(s!=null){
            String ss = s.toString().trim();
            if(ss.length()==0){
                return 0L;
            }else{
                return Long.parseLong(ss);
            }
        }else{
            return 0L;
        }
    }

}
