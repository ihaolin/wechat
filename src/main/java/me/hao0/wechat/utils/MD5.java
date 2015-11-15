package me.hao0.wechat.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Using Google Hasing
 */
@Deprecated
public class MD5 {

    private static MessageDigest md5Digest = null;

    private static final String salt = "qgk8(Y1*R";

    static {
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
    }

    public static String generate(String source, boolean withSalt) {
        if (source == null) {
            return null;
        }
        if (withSalt) {
            source = source + salt;
        }
        return digest(source);
    }

    private static String digest(String source) {
        if (source == null) {
            return null;
        }
        try {
            MessageDigest md5 = (MessageDigest) md5Digest.clone();
            md5.update(source.getBytes("UTF-8"));
            byte[] bs =  md5.digest();
            return byte2hex(bs);
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("clone of MD5 not supported", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("endoding utf-8 not supported", e);
        }
    }

    private static String byte2hex(byte[] bs) {
        String hs = "";
        String stmp;
        for (byte b : bs){
            stmp = (Integer.toHexString(b & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }
}