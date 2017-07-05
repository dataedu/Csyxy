package com.dk.edu.core.util.security;

import java.util.Iterator;
import java.util.Map;

public class Signature {
	
	
	private final static String SECRET_KEY = "dake_oa_app_key";
	
	public static Map<String, String> encryptRequestParam(Map<String, String> requestParam) {
		for (Iterator<String> iterator = requestParam.keySet().iterator(); iterator.hasNext();) {
			String nameKey = iterator.next();
			String text = requestParam.get(nameKey);
			try {
				requestParam.put(nameKey, CipherUtils.symmetryEncrypt(text, SECRET_KEY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return requestParam;
	};
	
	public static Map<String, String> decryptRequestParam(Map<String, String> requestParam) {
		for (Iterator<String> iterator = requestParam.keySet().iterator(); iterator.hasNext();) {
			String nameKey = iterator.next();
			String text = requestParam.get(nameKey);
			try {
				requestParam.put(nameKey, CipherUtils.symmetryDecrypt(text, SECRET_KEY));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return requestParam;
	}
	
	public static String decrypt(String text) {
		try {
			return CipherUtils.symmetryDecrypt(Base64Utils.decodeFilter(text), SECRET_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public static String encrypt(String text) {
		try {
			return Base64Utils.encodeFilter(CipherUtils.symmetryEncrypt(text, SECRET_KEY));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}
	
	public static void main(String[] args) {
	    System.out.println(Signature.encrypt("001|"+Signature.encrypt("dake_oa_app_key")+"|"+System.currentTimeMillis()));
	    
	    System.out.println(Signature.decrypt("aJfMFMGP5FU_"));
    }
	
}

