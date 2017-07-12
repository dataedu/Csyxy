package com.dk.mp.core.util.security;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Base64Utils {
	
	
	private final static String[] BASE64_CHARS = new String[] { "/", "-", "=", "_", "+", "%2B" };
	
	private final static String ENCODING_UTF8 = "UTF-8";
	
	public static String encodeURL(String url) {
		return URLEncoder.encode(encodeFilter(Base64Utils.encode(url)));
	}
	
	public static String encodeFilter(String url) {
		for (int i = 0, len = BASE64_CHARS.length; i < len; i = i + 2) {
			url = StringUtils.replace(url, BASE64_CHARS[i], BASE64_CHARS[i + 1]);
		}
		return url;
	}
	
	public static String decodeURL(String url) {
		return Base64Utils.decode(decodeFilter(url));
	}
	
	public static String decodeFilter(String url) {
		for (int i = 0, len = BASE64_CHARS.length; i < len; i = i + 2) {
			url = StringUtils.replace(url, BASE64_CHARS[i + 1], BASE64_CHARS[i]);
		}
		return url;
	}
	
	public static String encode(String value) {
		String reulst = StringUtils.EMPTY;
		
		if (value != null) {
			try {
				reulst = Base64.encodeBase64String(value.getBytes(ENCODING_UTF8));
			} catch (UnsupportedEncodingException e) {
			}
		}
		
		return reulst;
	}
	
	public static String decode(String value) {
		String result = StringUtils.EMPTY;
		
		if (value != null) {
			try {
				result = new String(Base64.decodeBase64(value.getBytes(ENCODING_UTF8)), ENCODING_UTF8);
			} catch (UnsupportedEncodingException e) {
			}
		}
		
		return result;
	}
	
}
