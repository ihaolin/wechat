package me.hao0.wechat.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Using Guava Hasing
 */
@Deprecated
public class SHA1 {

    private static final char[] LETTERS = "0123456789ABCDEF".toCharArray();

    /**
     * 字符串SHA1签名
     * @param value 字符串
     * @return 签名
     */
	public static String generate(String value) {
		try {
			return hash(MessageDigest.getInstance("SHA1"), value);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private static String hash(MessageDigest digest,String src) {
		return toHexString(digest.digest(src.getBytes()));
	}
	
	private static String toHexString(byte[] bytes) {
		char[] values = new char[bytes.length * 2];
		int i=0;
		for(byte b : bytes) {
			values[i++] = LETTERS[((b & 0xF0) >>> 4)];
			values[i++] = LETTERS[b & 0xF];
		}
		return String.valueOf(values);
	}
}