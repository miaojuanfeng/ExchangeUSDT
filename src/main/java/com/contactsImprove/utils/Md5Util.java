package com.contactsImprove.utils;

import java.security.MessageDigest;

public class Md5Util {
	static String charset="UTF-8";
	public static String execute(String string) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("md5");
			md.update(string.getBytes(charset));
			byte[] md5Bytes = md.digest();
			return bytes2Hex(md5Bytes);
		} catch (Exception e) {
			LoggerUtil.error(e.toString(),e);
		}
		return null;
	}

	private static String bytes2Hex(byte[] byteArray) {
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if ((byteArray[i] >= 0) && (byteArray[i] < 16)) {
				strBuf.append("0");
			}
			strBuf.append(Integer.toHexString(byteArray[i] & 0xFF));
		}
		return strBuf.toString();
	}
}
