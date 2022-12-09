package com.zerobasedomain.util;

import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.tomcat.util.codec.binary.Base64;

public class Aes256Util {

	// getInstance 메서드에 Cipher 객체를 AES 암호화, CBC operation mode, PKCS5 padding scheme로 초기화하라고 요청한다.
	public static String alg = "AES/CBC/PKCS5Padding";
	private static final String KEY = "ZEROBASEKEYISSECRETKEYZZ";
	private static final String IV = KEY.substring(0, 16);

	public static String encrypt(String text) {
		try {
			// Cipher: 암호화/복호화를 해주는 클래스 (Cipher = 암호)
			Cipher cipher = Cipher.getInstance(alg); // 변환형태 입력

			// Byte 형태로 인코딩 & 암호화
			// 인코딩된 키, 키의 인코딩 포맷, 암호화 알고리즘을 포함
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes());

			// cipher 객체를 암호화 모드로 초기화
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

			// doFinal: 암호화 또는 복호화된 메세지를 포함한 byte 배열을 반환
			byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
			// 강도높은 암호화를 위해 encrypted 된 것을 다시 Base64로 변환
			return Base64.encodeBase64String(encrypted);
		} catch (Exception e) {
			return null;
		}
	}

	public static String decrypt(String cipherText) {
		try {
			Cipher cipher = Cipher.getInstance(alg);
			SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(
				IV.getBytes(StandardCharsets.UTF_8));
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

			// 암호화 한 후 Base64 String으로 변환했던 것을 다시 byte로 역변환.
			byte[] decodeBytes = Base64.decodeBase64(cipherText);

			// byte로 바꾼 것을 DECRYPT_MODE로 지정된 Cipher를 이용해 복호화
			byte[] decrypted = cipher.doFinal(decodeBytes);
			return new String(decrypted, StandardCharsets.UTF_8);
		} catch (Exception e) {
			return null;
		}
	}
}
