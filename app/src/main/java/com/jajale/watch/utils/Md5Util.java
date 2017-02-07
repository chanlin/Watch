package com.jajale.watch.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Md5Util {
	/**
	 * /*** MD5加码 生成32位md5码
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			L.d(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();

	}

	/**
	 * 加密解密算法 执行一次加密，两次解密
	 */
	public static String convert(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		return new String(a);
	}

	/**
	 * 字典排序MD5加密
	 */
	public static String stringsortmd5(String service, String imei) {

        String md5 = "";
		String str = imei + service;

		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			byte[] b = mDigest.digest();

			BigInteger bigInteger = new BigInteger(1, b);
			md5 = bigInteger.toString(16);

			for (int i = md5.length(); i < 32; i++) {
				md5 = "0" + md5;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md5;
	}

    /**
     * 字典排序MD5加密
     */
    public static String stringsortmd5(String service, String imei, String data) {
        ArrayList<String> list=new ArrayList<String>();

        list.add(imei);
        if (!"".equals(data)) {
            list.add(data);
        }
        Collections.sort(list);

        String str = "";
//        String md5 = "";
        for(int i=0;i<list.size();i++){
            str += list.get(i);
        }
        str += service;
		L.e("sign==" + str);

		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			L.d(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		L.e("sign==?" + hexValue.toString());
		return hexValue.toString();

//        try {
//            MessageDigest mDigest = MessageDigest.getInstance("MD5");
//            mDigest.update(str.getBytes());
//            byte[] b = mDigest.digest();
//
//            BigInteger bigInteger = new BigInteger(1, b);
//            md5 = bigInteger.toString(16);
//
//            for (int i = md5.length(); i < 32; i++) {
//                md5 = "0" + md5;
//            }
//        } catch (NoSuchAlgorithmException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return md5;
    }

	/**
	 * 字典排序MD5加密
	 */
	public static String stringsortmd5(String service, String imei, String data , String data2) {
		ArrayList<String> list=new ArrayList<String>();

		list.add(imei);
		if (!"".equals(data)) {
			list.add(data);
		}
		if (!"".equals(data2)) {
			list.add(data2);
		}
		Collections.sort(list);

		String str = "";
		String md5 = "";
		for(int i=0;i<list.size();i++){
			str += list.get(i);
		}
		str += service;

		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			byte[] b = mDigest.digest();

			BigInteger bigInteger = new BigInteger(1, b);
			md5 = bigInteger.toString(16);

			for (int i = md5.length(); i < 32; i++) {
				md5 = "0" + md5;
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return md5;
	}

	/**
	 * 字典排序MD5加密
	 */
	public static String stringmd5(String data1, String data2) {

		String str = "";
		String md5 = "";
		str = data1 + data2;

		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			byte[] b = mDigest.digest();

			BigInteger bigInteger = new BigInteger(1, b);
			md5 = bigInteger.toString(16);

			for (int i = md5.length(); i < 32; i++) {
				md5 = "0" + md5;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

	/**
	 * 字典排序MD5加密
	 */
	public static String stringmd5(String data1, String data2, String data3) {

		String str = "";
		String md5 = "";
		str = data1 + data2 + data3;

		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			byte[] b = mDigest.digest();

			BigInteger bigInteger = new BigInteger(1, b);
			md5 = bigInteger.toString(16);

			for (int i = md5.length(); i < 32; i++) {
				md5 = "0" + md5;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}

	/**
	 * 字典排序MD5加密
	 */
	public static String stringmd5(String data1, String data2, String data3, String data4) {

		String str = "";
		String md5 = "";
		str = data1 + data2 + data3 + data4;

		try {
			MessageDigest mDigest = MessageDigest.getInstance("MD5");
			mDigest.update(str.getBytes());
			byte[] b = mDigest.digest();

			BigInteger bigInteger = new BigInteger(1, b);
			md5 = bigInteger.toString(16);

			for (int i = md5.length(); i < 32; i++) {
				md5 = "0" + md5;
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return md5;
	}
}
