package com.dgut.utils;

import java.util.Random;

public class RandomUtil {
    public static Integer randomOid(int oId){
        int flag = new Random().nextInt(9999);
        if (flag < 1000)
            flag += 1000;
        return oId+flag;
    }
    public static Integer randomCode(){
        int flag = new Random().nextInt(999999);
        if (flag < 100000)
            flag += 100000;
        return flag;
    }

}
