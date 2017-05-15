package com.huotu.ucphp;

import com.huotu.ucphp.config.UCenterConfig;
import me.jiangcai.lib.test.SpringWebTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

/**
 * Created by xyr on 2017/5/12.
 */
@ContextConfiguration(classes = {UCenterConfig.class})
@WebAppConfiguration
public class BaseTest extends SpringWebTest {

    public static String url = "http://bbs.51huotao.com/uc/index.php?__times__=1";
    public static int limit = 500000;
    public static String post = "m=user&a=logincheck&inajax=2&release=20170101&input=48dem81vMNs6UFsfuZZ3Zxv1S%" +
            "2BBOY%2FXT%2Fm6y%2BidrTYtedrUZki0WH1wsr1L5N5%2B7ubNupPu1r5UkV7rsM9RZVccNt6wMvD9arG6BxLmi2ZpSw5YYsjI" +
            "HVwPLfNDktin0Wk15gnQJ%2Fp%2BP99zXYX7J31ikADO9&appid=1";
    public static String UC_IP = "127.0.0.1";


    public String uc_user_synlogin() {
        return uc_fopen(url, limit, post, "", true, UC_IP, 20, true);
    }

    public String uc_fopen(String url, int limit, String post, String cookie, boolean bysocket ,String ip , int timeout , boolean block ) {
        String result = "";
        URL matches;
        String host="";
        String path="";
        int port = 80;
        try {
            matches = new URL(url);
            host = matches.getHost();
            path = matches.getPath()!=null? matches.getPath()+(matches.getQuery()!=null?"?"+matches.getQuery():""):"/";
            if( matches.getPort()>0 ) port = matches.getPort();
        } catch (MalformedURLException e1) {

        }


        StringBuffer out = new StringBuffer();
        if(post!=null && post.length()>0) {
            out.append("POST ").append(path).append(" HTTP/1.0\r\n");
            out.append("Accept: */*\r\n");
            out.append("Accept-Language: zh-cn\r\n");
            out.append("Content-Type: application/x-www-form-urlencoded\r\n");
            out.append("User-Agent: \r\n");
            out.append("Host: ").append(host).append("\r\n");
            out.append("Content-Length: ").append(post.length()).append("\r\n");
            out.append("Connection: Close\r\n");
            out.append("Cache-Control: no-cache\r\n");
            out.append("Cookie: \r\n\r\n");
            out.append(post);
        } else {
            out.append("GET $path HTTP/1.0\r\n");
            out.append( "Accept: */*\r\n");
            out.append("Accept-Language: zh-cn\r\n");
            out.append("User-Agent: Java/1.5.0_01\r\n");
            out.append("Host: $host\r\n");
            out.append("Connection: Close\r\n");
            out.append("Cookie: $cookie\r\n\r\n");
        }

        try{
            Socket fp = new Socket(ip!=null && ip.length()>10? ip : host, port );
            if(!fp.isConnected()) {
                return "";//note $errstr : $errno \r\n
            } else {

                OutputStream os = fp.getOutputStream();
                os.write(out.toString().getBytes());

                InputStream ins = fp.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(ins));
                while (true) {
                    String $header = reader.readLine();
                    if($header == null || $header.equals("") || $header == "\r\n" || $header == "\n") {
                        break;
                    }
                }

                while (true) {
                    String data = reader.readLine();
                    if(data == null || data.equals("") ) {
                        break;
                    }else{
                        result += data;
                    }
                }

                fp.close();
            }
        }catch (IOException e) {

        }
        return result;
    }

}
