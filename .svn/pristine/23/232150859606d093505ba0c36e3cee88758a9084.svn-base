package com.contactsImprove.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Cipher;
import org.apache.commons.io.IOUtils;

import com.alibaba.fastjson.JSON;
import com.contactsImprove.constant.MerchantOrderConst.Payment;




public class RSAUtils {
	
	 	public static final String CHARSET = "UTF-8";	    
	    public static final String RSA_ALGORITHM = "RSA";	    
	    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";	    
	    private static final String MY_PRIVATE_KEY="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMQF10jOvwUs7qLxZreoLtMsK31igLSeEueSJqzOLClfcBIbfL2KncERCg14cIYV8bfv/jeNmFGv4NJB62qNnDarK1i/JaHuZiFc1+A9ySNHuEz2UILVr1YZBC4ZkpTcIV8SK+LHQ/dN8V0Y5yU97ZUePIdSr9jtzSzLGdnUxo3/AgMBAAECgYBg1Z3XSsLUlLDCb8xVV9Dh4wUM6Lc6AojGOs2+Og9Y1NuxJkEGBU8PDBLSOrgjlP2W5wjHerxPRjoixAu+1HvhKpCRIeRnSPE3JxGLn12bRMU1Zv2BAQWy0U9Zmiz7cnHFVPXl0XpLnCoIy4LAkAWQp1gZSy8x56x+/lI7zTTt+QJBAP9BmlMbkFbcSHmmedrte0AqGg5Pqqjhp1P6SyXFW643ykV2LXd1LB8PJ0hCl5/y/rMoXnKB20l8tGu1a+B3YbMCQQDEmA44H5lXpKsL4Ovo/wtp8VLn2pKuYiDdVCETfVivy+41w80F24QRnxPHC/8SxvB90Czd3emZCQm4BBPg3gSFAkAsD/YE3KA47kOwhDVKvm3lMJ9Y0xXBm1pYG7+3IG9oDodjIDhPA7H/Fo2QYrZrzPV8lb5gA9yHvfgjE6kwkUsTAkEAsuCj938Q+IUVOV6zaLI/wWraA6qGUnNo8dntqp0fx/0nRvZco1rERYBX74HD2mL3SZLUcB/Nv/hQRoY3TCDXPQJAOjoNeqNvhUp0Vt6kNEt71qTN7zVHkD3FU71DVpkmJSLZKZH7xGgy1+tEBvvvG5q/IdNOB4bWoVXICgd6FNBB6w==";
	    public static final String MY_PUBLIC_KEY="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDEBddIzr8FLO6i8Wa3qC7TLCt9YoC0nhLnkiasziwpX3ASG3y9ip3BEQoNeHCGFfG37/43jZhRr+DSQetqjZw2qytYvyWh7mYhXNfgPckjR7hM9lCC1a9WGQQuGZKU3CFfEivix0P3TfFdGOclPe2VHjyHUq/Y7c0syxnZ1MaN/wIDAQAB";	    
	    private static final String OPPOSITE_PUBLIC_KEY="";
	    private static final int commercial_key_size=2048;
	    public static final String key_public="publicKey";
	    public static final String key_private="privateKey";
	    
	    /**
	     *	创建商户公钥私钥 对 。 
	     * @return
	     */	   
	    public static Map<String, String> createCommercialKeys(){
	     
	        return createKeys(commercial_key_size);
	    }
	    	  
	    public static Map<String, String> createKeys(int keySize){
	        //为RSA算法创建一个KeyPairGenerator对象
	        KeyPairGenerator kpg;
	        try{
	            kpg = KeyPairGenerator.getInstance(RSA_ALGORITHM);
	        }catch(NoSuchAlgorithmException e){
	            throw new IllegalArgumentException("No such algorithm-->[" + RSA_ALGORITHM + "]");
	        }
	        //初始化KeyPairGenerator对象,密钥长度
	        kpg.initialize(keySize);
	        //生成密匙对
	        KeyPair keyPair = kpg.generateKeyPair();
	        //得到公钥
	        Key publicKey = keyPair.getPublic();
	        String publicKeyStr= Base64.getEncoder().encodeToString(publicKey.getEncoded());
	        //得到私钥
	        Key privateKey = keyPair.getPrivate();	        
	        String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());	        
	        Map<String, String> keyPairMap = new HashMap<String, String>();
	        keyPairMap.put(key_public, publicKeyStr);
	        keyPairMap.put(key_private, privateKeyStr);

	        return keyPairMap;
	    }

	    /**
	     * 得到公钥
	     * @param publicKey 密钥字符串（经过base64编码）
	     * @throws IOException 
	     * @throws Exception
	     */
	    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
	        //通过X509编码的Key指令获得公钥对象
	        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
	        
	        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode((publicKey)));
	        
	        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
	       
	        return key;
	    }

	    /**
	     * 得到私钥
	     * @param privateKey 密钥字符串（经过base64编码）
	     * @throws Exception
	     */
	    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
	        //通过PKCS#8编码的Key指令获得私钥对象
	        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
	        
	        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey));
	        	       	        
	        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
	        return key;
	    }

	    /**
	     * 公钥加密
	     * @param data
	     * @param publicKey
	     * @return
	     */
	    public static String publicEncrypt(String data, RSAPublicKey publicKey){
	        try{
	        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	      //    Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
	
	        //  return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes(CHARSET)));
	
	            return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
	        }catch(Exception e){
	            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
	        }
	    }

	    /**
	     * 私钥解密
	     * @param data
	     * @param privateKey
	     * @return
	     */

	    public static String privateDecrypt(String data, RSAPrivateKey privateKey){
	        try{	        	
	        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	            cipher.init(Cipher.DECRYPT_MODE, privateKey);
	      //      return new String(cipher.doFinal(Base64.getDecoder().decode(data)));	        
	            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), privateKey.getModulus().bitLength()), CHARSET);
	        }catch(Exception e){
	            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
	        }
	    }

	    /**
	     * 私钥加密
	     * @param data
	     * @param privateKey
	     * @return
	     */

	    public static String privateEncrypt(String data, RSAPrivateKey privateKey){
	        try{
	        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	          //  Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
	            cipher.init(Cipher.ENCRYPT_MODE, privateKey);	            
	        
	           // return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
	            return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength()));
	        }catch(Exception e){
	            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
	        }
	    }

	    /**
	     * 公钥解密
	     * @param data
	     * @param publicKey
	     * @return
	     */

	    public static String publicDecrypt(String data, RSAPublicKey publicKey){
	        try{
	        	Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	           // Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
	            cipher.init(Cipher.DECRYPT_MODE, publicKey);
	
	          //  return new String(cipher.doFinal(Base64.getDecoder().decode(data)));
	            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, Base64.getDecoder().decode(data), publicKey.getModulus().bitLength()), CHARSET);
	        }catch(Exception e){
	            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
	        }
	    }

	    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
	        int maxBlock = 0;
	        if(opmode == Cipher.DECRYPT_MODE){
	            maxBlock = keySize / 8;
	        }else{
	            maxBlock = keySize / 8 - 11;
	        }
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        int offSet = 0;
	        byte[] buff;
	        int i = 0;
	        try{
	            while(datas.length > offSet){
	                if(datas.length-offSet > maxBlock){
	                    buff = cipher.doFinal(datas, offSet, maxBlock);
	                }else{
	                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
	                }
	                out.write(buff, 0, buff.length);
	                i++;
	                offSet = i * maxBlock;
	            }
	        }catch(Exception e){
	            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
	        }
	        byte[] resultDatas = out.toByteArray();
	        IOUtils.closeQuietly(out);
	        return resultDatas;
	    }
	    
	    public static String privateDecrypt(String encodedData) {
	    	
	    	try {
				return RSAUtils.privateDecrypt(encodedData, getPrivateKey(RSAUtils.MY_PRIVATE_KEY));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return null;	    	
	    }
	    
	    public static String privateEncrypt(String encodedData) {
	    	
	    	try {
				return privateEncrypt(encodedData, getPrivateKey(MY_PRIVATE_KEY));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	return null;	    	
	    }
	    
	    public static String publicEncrypt(String data) {
	    	try {
				try {
					return publicEncrypt(data, getPublicKey(OPPOSITE_PUBLIC_KEY));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	    	
	    	return null;	    	
	    }
	    
	    /**
	     * 商户私钥签名，用商户公钥验签
	     * @param paraStr   除签名外的参数排序字符串
	     * @param sign	     签名值
	     * @param public_key
	     * @return
	     */
	    public static boolean verifySignature(String paraOrderStr,String sign,String publicKey) {
	    	try {
	    		if(UrlEncoderUtils.hasUrlEncoded(sign)) {
	    			sign=java.net.URLDecoder.decode(sign, CHARSET);
	    		}
		    	//指定签名类型
		    	Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
		    	signature.initVerify(getPublicKey(publicKey));//放入公钥
		    	signature.update(paraOrderStr .getBytes(CHARSET));//放入数据
		    	//验签结果
		    	return signature.verify(Base64.getDecoder().decode(sign));
	    	}catch(Exception e) {
	    		LoggerUtil.error(e.getMessage(), e);
	    	}
	    	return false;	    	
	    }
	    /**
	     * 用商户私钥签名，对方公钥验签。
	     * @param paraMap
	     * @param private_key
	     * @return
	     */
	    public static String signature(String paraOrderStr,String privateKey) {	   
	    	try {
		    	/* 计算验签字段, sign字段不参与签名 map转String方法可以自定义*/
		    	Signature signature = Signature.getInstance(SIGN_ALGORITHMS);	    	
		    	signature.initSign(getPrivateKey(privateKey));//设置私钥
		    	//签名和加密一样 要以字节形式 utf-8字符集得到字节
		    	signature.update(paraOrderStr.getBytes(CHARSET));
		    	//得到base64编码的签名后的字段
		    	String sign = Base64.getEncoder().encodeToString(signature.sign());
		    	return sign;
	    	}catch(Exception e) {
	    		LoggerUtil.error(e.getMessage(), e);
	    	}	    	
	    	return null;	    	
	    }
	   	    
	    public static void main (String[] args) throws Exception {
//	    	/* 1. 准备发送的数据 为保证顺序正确这里使用TreeMap*/
//	    	TreeMap<String, String> paramap = new TreeMap<String, String>();
//			paramap.put("outTradeNo", "369852147");
//			paramap.put("merchantUserId", "2018112710100000001");
//			paramap.put("currencyType", "USTD");
//			paramap.put("amount", "20");
//			paramap.put("version", "1.0");
//			paramap.put("subject", "购买USTD");
//			paramap.put("notifyUrl", "");
//			paramap.put("timestamp", DateTools.DateToStr2(new Date()));
//			paramap.put("paymentType", Payment.alipay.code);
//	    	/* 2. 计算验签字段, sign字段不参与签名 map转String方法可以自定义*/
////	    	String line = JSON.toJSONString(paramap);
//	    	String line=OrderUtil.createParaOrderStr(paramap);
//	    	Signature signature = Signature.getInstance(SIGN_ALGORITHMS);	    	
//	    	signature.initSign(getPrivateKey(MY_PRIVATE_KEY));//设置私钥
//	    	//签名和加密一样 要以字节形式 utf-8字符集得到字节
//	    	signature.update(line.getBytes(CHARSET));
//	    	//得到base64编码的签名后的字段
//	    	String sign = Base64.getEncoder().encodeToString(signature.sign());
//	    	paramap.put("sign", sign);	    	    
//	    	System.out.println(verifySignature(line,sign,MY_PUBLIC_KEY));
	    	
	    	for(int i=0;i<1;i++) {
		        Map<String, String> keyMap = RSAUtils.createKeys(2048);
		        String  publicKey = keyMap.get("publicKey");
		        String  privateKey = keyMap.get("privateKey");
		        System.out.println(publicKey.length()+"公钥: \n\r" + publicKey);
		        System.out.println(privateKey.length()+"私钥： \n\r" + privateKey);
	    	}
//
//	        System.out.println("公钥加密——私钥解密");
//	        String str = "{\"moneyDealer\":0,\"id\":9,\"userName\":null,\"token\":\"eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJ0b2tlbmlkIiwiaWF0IjoxNTMzNTUwMzk0LCJzdWIiOiJ7XCJwaG9uZU51bWJlclwiOlwiMTM3NjA4NDg1NjlcIixcInRva2VuXCI6XCIxMzc2MDg0ODU2OTE1MzM1NTAzOTQyODNkM2NiYjI4MmM2ZjM0ODUyOTNhMDU2YzhlNDIyZTBiNFwiLFwidXNlcmlkXCI6OX0iLCJleHAiOjE1MzM1NTA5OTR9.-kAGqt6W1XHRt5nYZKsRSFIW7-wg3dP_cFEDcjqBRLY\"}";
//	        String str = "{\"moneyDealer\":0,\"id\":9,\"userName\":null,\"token\":\"eyJ0eXBlIjoiSldUIiwiYWxnIjoiS\"}";
//	        System.out.println("\r明文：\r\n" + str);
//	        System.out.println("\r明文大小：\r\n" + str.getBytes().length);
//	        String encodedData = RSAUtils.publicEncrypt(str, RSAUtils.getPublicKey(RSAUtils.MY_PUBLIC_KEY));
	        
//	        String encodedData = RSAUtils.privateEncrypt(str, RSAUtils.getPrivateKey(RSAUtils.MY_PRIVATE_KEY));
//	        System.out.println("密文：\r\n" + encodedData);
//	        System.out.println("密文：\r\n" + encodedData.length());
//	        String decodedData = RSAUtils.publicDecrypt(encodedData, RSAUtils.getPublicKey(RSAUtils.MY_PUBLIC_KEY));
//	        String decodedData = RSAUtils.privateDecrypt(encodedData, RSAUtils.getPrivateKey(RSAUtils.MY_PRIVATE_KEY));        
//	        System.out.println("解密后文字: \r\n" + decodedData);	    	
	        
	        
	    	
//	    	String str="eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJqdGkiOiJ0b2tlbmlkIiwiaWF0IjoxNTMzMjE1MzQ3LCJzdWIiOiJ7XCJwaG9uZU51bWJlclwiOlwiMTgwNDY3NzE3ODdcIixcInRva2VuXCI6XCIxODA0Njc3MTc4NzE1MzMyMTUzNDcyNjRlMmI1NjMzY2U1NzA0Y2NkYjMwY2RjNzg0NWFmMWNhYlwiLFwidXNlcmlkXCI6MTh9IiwiZXhwIjoxNTMzMjE1OTQ3fQ.afVjue9daDlZrcYO8APSLhTZ3EsbMqMGhsuIg20Q7z4";
////	    			
//	    	byte[] test=Base64.decodeBase64(MY_PUBLIC_KEY);
//	    	System.out.println(Base64.decodeBase64(MY_PUBLIC_KEY));
//	    	System.out.println(Base64.decodeBase64(MY_PUBLIC_KEY));

	    }

}
