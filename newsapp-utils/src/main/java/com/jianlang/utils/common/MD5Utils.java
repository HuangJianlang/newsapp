package com.jianlang.utils.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public final static String encode(String str){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());
            byte mdBytes[] = messageDigest.digest();
            String hash = "";
            for (int i = 0; i < mdBytes.length; i++) {
                int temp;
                //如果有小于0的字节,则转换为正数
                if (mdBytes[i] < 0)
                    temp = 256 + mdBytes[i];
                else
                    temp = mdBytes[i];
                if (temp < 16)
                    hash += "0";
                //将字节转换为16进制后，转换为字符串
                hash += Integer.toString(temp, 16);
            }
            return hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String encodeWithSalt(String numStr, String salt){
        return encode(encode(numStr) + salt);
    }

}
