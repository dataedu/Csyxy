package com.dk.mp.core.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
		"e", "f" };

public final static byte[] key = { -0x23, 0x36, 0x5e, 0x75, -0x24, 0x5e, -0x26, 0x31, 0x32, 0x4b, -0x77, 0x32,
		-0x4c, 0x2e, 0x64, -0x4f, 0x69, 0x42, 0x3a, -0x5c, 0x22, -0x3c, 0x39, 0x44 };

public static String encrypt(String str) throws UnsupportedEncodingException {
	byte[] b = str.getBytes("utf-8");
	StringBuffer sb = new StringBuffer(b.length * 2);
	for (int i = 0; i < b.length; i++) {
		int c = b[i] ^ (key[i % key.length]);
		sb.append(hexDigits[(c & 0xf0) >>> 4]);
		sb.append(hexDigits[c & 0x0f]);
	}
	return sb.toString();
}

public static String decrypt(String str) throws UnsupportedEncodingException {
	int len = str.length() / 2;
	if (str.length() == 0 || str.length() % 2 != 0)
		return null;
	byte[] b = new byte[len];
	for (int i = 0; i < len; i++) {
		b[i] = (byte) (Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16) ^ (key[i % key.length]));
	}
	return new String(b, "utf-8");
}

public static String toHexString(byte[] b) {
	StringBuffer sb = new StringBuffer(b.length * 2);
	for (int i = 0; i < b.length; i++) {
		sb.append(hexDigits[(b[i] & 0xf0) >>> 4]);
		sb.append(hexDigits[b[i] & 0x0f]);
	}
	return sb.toString();
}

public static String MD5(String str) throws NoSuchAlgorithmException {
	MessageDigest md = MessageDigest.getInstance("MD5");
	return toHexString(md.digest(str.getBytes()));
}


public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
	

	System.out.println(MD5Util.MD5("19840309"));
}
}
