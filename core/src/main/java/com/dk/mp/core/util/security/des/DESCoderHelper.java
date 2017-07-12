/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dk.mp.core.util.security.des;


/**
 * des加密解密.
 * @since jdk1.5
 * @version 2013-3-5
 
 */
public class DESCoderHelper {
	protected static String charSet = "UTF-8";

	/**
	 * 初始化.
	 */
	public DESCoderHelper() {
	}

	/**
	 * 加密.
	 * <p>将一段字符串text，以publicKey为密钥进行 对称加密。例如： </p>
	 * <p>需加密字符串：123，密钥：284d19fc-3723-4dae-9faf-684c976acc9f，加密后数据：YkOZSlLfj5Y=</p>
	 * @param data 明文
	 * @param secretKey 密钥
	 * @return 密文
	 */
	@SuppressWarnings("static-access")
	public static String encrypt(String data, String secretKey) {
		String str = null;
		if (null != data && !"".equals(data)) {
			try {
				str = DESCoder.encryptBASE64(DESCoder.encrypt(data.getBytes(charSet), secretKey)).replaceAll("\n", "")
						.replaceAll("\r", "");
			} catch (Exception e) {
			}
		} else {
			str = data;
		}
		return str;
	}

	/**
	 * 解密.
	 * <p>将一段字符串text，以publicKey为密钥进行 对称解密。例如： </p>
	 * <p>需解密字符串：YkOZSlLfj5Y=，密钥：284d19fc-3723-4dae-9faf-684c976acc9f，解密后数据：123=</p>
	 * @param data 密文
	 * @param secretKey 密钥
	 * @return 明文
	 */
	@SuppressWarnings("static-access")
	public static String decrypt(String data, String secretKey) {
		String outputStr = null;
		if (null != data && !"".equals(data)) {
			try {
				outputStr = new String(DESCoder.decrypt(DESCoder.decryptBASE64(data), secretKey), charSet);
			} catch (Exception e) {
			}
		} else {
			outputStr = data;
		}
		return outputStr;
	}
}