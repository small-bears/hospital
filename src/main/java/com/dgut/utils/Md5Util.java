package com.dgut.utils;

import org.springframework.util.DigestUtils;

public class Md5Util {

    //盐，用于混交md5
    private static String salt = "asdwqAsd12_qS";

    public static String getMD5(String str) {
        String base = str + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }


}