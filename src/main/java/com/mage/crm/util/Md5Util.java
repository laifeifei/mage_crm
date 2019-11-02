package com.mage.crm.util;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 用md5加密算法进行加密，不能解密
 */
public class Md5Util {
    public static String encode(String pwd){
        String s = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            byte[] digest = messageDigest.digest(pwd.getBytes());
            s = Base64.encodeBase64String(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    return s;
    }

    /*public static void main(String[] args) {
        String encode = Md5Util.encode("123456");
        System.out.println(encode);
    }*/
}
