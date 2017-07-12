/**
 * 
 */
package com.dk.mp.core.util.security;

import com.dk.mp.core.util.security.des.DESCoderHelper;

import java.util.Date;
import java.util.UUID;

/**
 *加解密工具类 .
 * @since jdk1.5
 
 */
public class CipherUtils {

	

	/** 
	 * 对称加密.
	 * <p>将一段字符串text，以publicKey为密钥进行 对称加密。例如： </p>
	 * <p>需加密字符串：123，密钥：284d19fc-3723-4dae-9faf-684c976acc9f，加密后数据：YkOZSlLfj5Y=</p>
	 * @param text 明文
	 * @param secretKey  会话密钥
	 * @throws Exception Exception
	 * @return 加密数据
	 */
	public static String symmetryEncrypt(String text, String secretKey) throws Exception {
		return DESCoderHelper.encrypt(text, secretKey);
	}

	/** 
	 * 对称解密数据.
	 * <p>将一段字符串text，以publicKey为密钥进行 对称解密。例如： </p>
	 * <p>需解密字符串：YkOZSlLfj5Y=，密钥：284d19fc-3723-4dae-9faf-684c976acc9f，解密后数据：123=</p>
	 * @param text 密文
	 * @param secretKey  会话密钥
	 * @throws Exception Exception
	 * @return 解密数据
	 */
	public static String symmetryDecrypt(String text, String secretKey) throws Exception {
		return DESCoderHelper.decrypt(text, secretKey);
	}

	/**
	 * 随机生成会话的des密钥.
	 * <p>返回一段随机生成的uuid字符串。例如：284d19fc-3723-4dae-9faf-684c976acc9f</p>
	 * @return 会话的密钥
	 */
	public static String getSecretKey() {
		return UUID.randomUUID().toString();
	}

	
	//: /-->-,=-->_
	public static void main(String[]ss){
		try {
			
			String y=symmetryEncrypt("001|111111|"+new Date().getTime(), "jsdkjykjyxgs");
			System.out.println(y);
			String base=Base64.getBase64(y);
			System.out.println(base);
			base=base.replace("/", "-").replace("=", "_");
			System.out.println(base);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
