/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dk.mp.core.util.security.des;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * .
 * @since jdk1.5
 * @version 2013-3-5
 
 */
public abstract class DESCoder extends Coder {

	private static final String ALGORITHM = "DES";

	/**
	* 转换密钥.
	* @param key byte[]
	* @return Key
	* @throws Exception Exception
	*/
	private static Key toKey(byte[] key) throws Exception {
		DESKeySpec dks = new DESKeySpec(key);
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
		SecretKey secretKey = keyFactory.generateSecret(dks);
		// 当使用其他对称加密算法时，如AES、Blowfish等算法时，用下述代码替换上述三行代码
		// SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
		return secretKey;
	}

	/**
	* 解密.
	* @param data byte[]
	* @param key String
	* @return byte[]
	* @throws  Exception Exception
	*/
	public static byte[] decrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, k);
		return cipher.doFinal(data);
	}

	/**
	* 加密.
	* @param data  byte[]
	* @param key String
	* @return byte[]
	* @throws Exception Exception
	*/
	public static byte[] encrypt(byte[] data, String key) throws Exception {
		Key k = toKey(decryptBASE64(key));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, k);
		return cipher.doFinal(data);
	}
	
	
public static void main(String[] args) throws Exception {
System.out.println(DESCoder.encrypt(new String("dzhang").getBytes(), "123456"));	
//System.out.println(DESCoder.decrypt(new String("[B@4139eeda").getBytes(), "123456"));	


}

}