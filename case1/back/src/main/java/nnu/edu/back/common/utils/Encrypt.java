package nnu.edu.back.common.utils;

import nnu.edu.back.common.exception.MyException;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: Yiming
 * @Date: 2023/07/25/23:47
 * @Description:
 */
public class Encrypt {
    public static String md5(String pwd) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(pwd.getBytes());   //计算MD5函数

            return new BigInteger(1, messageDigest.digest()).toString(16);          //转成16进制数
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new MyException(-99, "md5加密出错!");
        }
    }
}
