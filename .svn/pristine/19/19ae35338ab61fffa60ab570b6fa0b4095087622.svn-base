package com.contactsImprove.utils;

import java.security.Key;
import java.util.Date;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUtil {
	
	private final static String base64Secret = "MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
	
	/**
	 * 创建jwt
	 * 
	 * @param id
	 * @param subject
	 * @param ttlMillis
	 * @return
	 */
	public static String createJWT(String phoneNumber, String token,
			Integer id, long ttlMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		Key signingKey = new SecretKeySpec(DatatypeConverter.parseBase64Binary(base64Secret), signatureAlgorithm.getJcaName());
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("phoneNumber", phoneNumber);
		jsonObject.put("token", token);
		jsonObject.put("userId", id);
		// 添加构成JWT的参数
		JwtBuilder builder = Jwts.builder().setHeaderParam("type", "JWT")
			.setId("tokenId")
			.setIssuedAt(new Date())// 创建时间
			.setSubject(jsonObject.toString())// 个人的一些信息
			.signWith(signatureAlgorithm, signingKey);
		jsonObject=null;
		return builder.compact();
	}

	/**
	 * 解析jwt
	 * 
	 * @param jwt
	 * @return
	 */
	public static Claims pareseJWT(String jsonWebToken) {
		try {
			JwtParser jp=Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret));
			Claims claims = jp.parseClaimsJws(jsonWebToken).getBody();
			return claims;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 验证jwt
	 * 
	 * @param token
	 * @return
	 */
	public static Integer validate(String token, HttpServletRequest request) {
		if (token != null) {
			try {
				Claims claims = JwtUtil.pareseJWT(token);			
				JSONObject jsonObject = JSONObject.parseObject(claims.getSubject());
				String phoneNumber = jsonObject.getString("phoneNumber");
				Integer userid = jsonObject.getInteger("userId");			
				String token_eq = RedisUtil.getValue(phoneNumber + "_login");
				jsonObject=null;
				phoneNumber=null;
				claims=null;
				if (token.equals(token_eq)) {
					return userid;
				}
			} catch (Exception e) {
				return 0;
			}
		}
		return 0;
	}
}
