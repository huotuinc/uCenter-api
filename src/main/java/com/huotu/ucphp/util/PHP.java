package com.huotu.ucphp.util;

import lombok.SneakyThrows;
import org.springframework.util.DigestUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.Base64;

/**
 * Created by xyr on 2017/5/12.
 */
public class PHP {

    @SneakyThrows({
            NoSuchPaddingException.class,
            NoSuchAlgorithmException.class,
            InvalidKeyException.class,
            BadPaddingException.class,
            IllegalBlockSizeException.class
    })
    public void auth(String input,String key) {
        int keyLength = 0;

        String cryptKey = MD5(MD5(key.substring(0,16))+input.substring(0,keyLength));
        Base64.Decoder mimeDecoder = Base64.getMimeDecoder();
        byte[] afterBase64 = mimeDecoder.decode(input.substring(keyLength));
        Cipher cipher= Cipher.getInstance("RC4");

        byte[] ascForCryptKey = cryptKey.getBytes(Charset.forName("ASCII"));
        byte[] secretKey = new byte[16];
        for (int i = 0; i < secretKey.length; i++) {
            secretKey[i] = ascForCryptKey[i % cryptKey.length()];
        }
        // 5,16
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(secretKey,"RC4"));

        byte[] result = cipher.doFinal(afterBase64);
        // 0-25 是用于校验
        byte[] temp = mimeDecoder.decode(input.substring(keyLength, input.length()));
        String finalReulst = new String(cipher.doFinal(temp));
        System.out.println(finalReulst);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    private static String MD5(String MD5) {
        return DigestUtils.md5DigestAsHex(MD5.getBytes("UTF-8"));
    }

//    function authcode($string, $operation = 'DECODE', $key = '', $expiry = 0) {
//
//        $ckey_length = 4;
//
//        $key = md5($key ? $key : UC_KEY);
//        $keya = md5(substr($key, 0, 16));
//        $keyb = md5(substr($key, 16, 16));
//        $keyc = $ckey_length ? ($operation == 'DECODE' ? substr($string, 0, $ckey_length):
//                substr(md5(microtime()), -$ckey_length)) : '';
//
//        $cryptkey = $keya.md5($keya.$keyc);
//        $key_length = strlen($cryptkey);
//
//        $string = $operation == 'DECODE' ? base64_decode(substr($string, $ckey_length)) :
//                sprintf('%010d', $expiry ? $expiry + time() : 0).substr(md5($string.$keyb), 0, 16).$string;
//        $string_length = strlen($string);
//
//        $result = '';
//        $box = range(0, 255);
//
//        $rndkey = array();
//        for($i = 0; $i <= 255; $i++) {
//            $rndkey[$i] = ord($cryptkey[$i % $key_length]);
//        }
//
//        for($j = $i = 0; $i < 256; $i++) {
//            $j = ($j + $box[$i] + $rndkey[$i]) % 256;
//            $tmp = $box[$i];
//            $box[$i] = $box[$j];
//            $box[$j] = $tmp;
//        }
//
//        for($a = $j = $i = 0; $i < $string_length; $i++) {
//            $a = ($a + 1) % 256;
//            $j = ($j + $box[$a]) % 256;
//            $tmp = $box[$a];
//            $box[$a] = $box[$j];
//            $box[$j] = $tmp;
//            $result .= chr(ord($string[$i]) ^ ($box[($box[$a] + $box[$j]) % 256]));
//        }
//
//        if($operation == 'DECODE') {
//            if((substr($result, 0, 10) == 0 || substr($result, 0, 10) - time() > 0) &&
//                    substr($result, 10, 16) == substr(md5(substr($result, 26).$keyb), 0, 16)) {
//                return substr($result, 26);
//            } else {
//                return '';
//            }
//        } else {
//            return $keyc.str_replace('=', '', base64_encode($result));
//        }
//
//    }


}
