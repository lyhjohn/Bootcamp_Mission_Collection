package com.zerobasedomain.config;

import com.zerobasedomain.domain.common.UserVo;
import com.zerobasedomain.domain.common.UserType;
import com.zerobasedomain.util.Aes256Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.Objects;

public class JwtAuthenticationProvider {

	private final String SECRET_KEY = "secretKey";
	private final long tokenValidTime = 1000L * 60 * 60 * 24; // 하루

	public String createToken(String userPk, Long id, UserType userType) {
		// userPk 암호화
		Claims claims = Jwts.claims().setSubject(Aes256Util.encrypt(userPk))
			.setId(Aes256Util.encrypt(id.toString()));
		claims.put("roles", userType);
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + tokenValidTime)) // 만료시간: 지금으로부터 하루 뒤
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY)
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

	public UserVo getUserVo(String token) {
		Claims c = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
		System.out.println("claim = " + c);
		return new UserVo(Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(c.getId()))),
			Aes256Util.decrypt(c.getSubject()));
	}
}
