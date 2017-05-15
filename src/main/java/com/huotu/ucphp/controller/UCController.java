package com.huotu.ucphp.controller;

import com.huotu.ucphp.service.UCUserService;
import com.huotu.ucphp.util.AuthCodeUtil;
import com.huotu.ucphp.util.PHP;
import com.huotu.ucphp.util.UCUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xyr on 2017/4/25.
 */
@Controller
public class UCController {

    public static boolean API_SYNLOGIN=true;		//note 同步登录 API 接口开关
    public static boolean API_SYNLOGOUT=true;		//note 同步登出 API 接口开关

    public static String API_RETURN_SUCCEED   =    "1";
    public static String API_RETURN_FAILED    =   "-1";
    public static String API_RETURN_FORBIDDEN =   "-2";

    @Autowired
    private UCUserService ucUserService;

    /**
     * 同步登录
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/uCenter/**")
    public void doRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String requestMapping = request.getParameter("a");
        if ("logincheck".equals(requestMapping)) { //同步登录

            String code = request.getParameter("input");
            code = code.replace(' ', '+');
            if (code == null) {
                response.getWriter().print(API_RETURN_FAILED);
                return;
            }

            Map<String, String> get = new HashMap<String, String>();
            //code =  new UCUtil().uc_authcode(code, "DECODE", null, 0);
            //new PHP().auth(code,"testKeyJustForForever");
            code = new AuthCodeUtil().authcodeDecode(code, "testKeyJustForForever");
            parse_str(code, get);

            if (get.isEmpty()) {
                response.getWriter().print("Invalid Request");
                return;
            }
            if(time() - tolong(get.get("time")) > 3600) {
                response.getWriter().print("Authracation has expiried");
                return;
            }

            if(!API_SYNLOGIN ) {
                response.getWriter().print(API_RETURN_FORBIDDEN);
                return;
            }

            String username = get.get("username");
            String password = get.get("password");

            if (!ucUserService.validUser(username, password)) {
                response.getWriter().print(API_RETURN_FAILED);
                return;
            }
            //同步登录 API 接口
            response.addHeader("P3P","CP=\"CURa ADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSP COR\"");

            int cookietime = 31536000;

            Cookie user = new Cookie("loginuser",get.get("username"));
            user.setMaxAge(cookietime);
            response.addCookie(user);
        } else if ("logincheck".equals(requestMapping)){ //同步登出
            String code = request.getParameter("input");
            code = code.replace(' ', '+');
            if (code == null) {
                response.getWriter().print(API_RETURN_FAILED);
                return;
            }

            Map<String, String> get = new HashMap<String, String>();
            code =  new AuthCodeUtil().authcodeDecode(code, "");
            parse_str(code, get);

            if (get.isEmpty()) {
                response.getWriter().print("Invalid Request");
                return;
            }
            if(time() - tolong(get.get("time")) > 3600) {
                response.getWriter().print("Authracation has expiried");
                return;
            }

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
