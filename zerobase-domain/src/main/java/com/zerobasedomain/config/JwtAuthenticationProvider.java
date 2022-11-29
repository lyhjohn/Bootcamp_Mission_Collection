package com.zerobasedomain.config;

import com.zerobasedomain.domain.common.UserVo;
import com.zerobasedomain.domain.common.UserType;
import com.zerobasedomain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {

	private final String SECRET_KEY = "secretKey";
	private final long tokenValidTime = 1000L * 60 * 60 * 24; // 하루

	public String createToken(String userPk, Long id, UserType userType) {
		// email. id를 인코딩해서 Claims 저장 (Claims에는 유저 정보가 들어감)
		Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk))
			.setId(Aes256Util.encrypt(id.toString()));
		claims.put("roles", userType); // role 저장
		Date now = new Date();

		// 토큰 생성
		return Jwts.builder()
			.setClaims(claims) // 유저정보
			.setIssuedAt(now) // 발행일
			.setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료시간: 지금으로부터 하루 뒤
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 알고리즘, 시크릿키 입력
			.compact();
	}

	// 토큰 만료 검증
	public boolean validateToken(String jwtToken) {
		try {
			Jws<Claims> claimsJws = Jwts.parser().setSigningKey(SECRET_KEY)
				.parseClaimsJws(jwtToken);
			return !claimsJws.getBody().getExpiration().before(new Date());
		} catch (Exception e) {
			return false;
		}
	}

	// 토큰에서 유저정보 가져오기
	public UserVo getUserVo(String token) {

		// 토큰 생성할 때 넣었던 시크릿키를 넣어줌
		Claims c = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		System.out.println("claim = " + c);
		// 토큰 만들 때 클레임에 이메일과 id를 subject, id로 넣었으니 마찬가지로 이 두개가 꺼내질 것
		System.out.println("c.getSubject() = " + Aes256Util.decrypt(c.getSubject()));
		System.out.println("c.getId() = " + Aes256Util.decrypt(c.getId()));
		System.out.println("getClass() = " + Aes256Util.decrypt(c.getSubject()).getClass());

		// requireNonNull: 이 부분에서 널이 발생 시 즉시 널포인트 에러 발생.
		// 원래라면 널이 나와도 뒤에서 이 널값을 사용하지 않는 이상 에러가 발생하지 않음
		return new UserVo(Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(c.getId()))),
			Aes256Util.decrypt(c.getSubject()));
	}

	public String getRoles(String token) {

		Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		return (String) claims.get("roles");
	}
}
