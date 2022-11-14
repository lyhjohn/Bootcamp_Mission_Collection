package zerobase.stock.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.stock.service.MemberService;

@Component
@RequiredArgsConstructor
public class TokenProvider {

	private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1 hour (60ms * 60ms * 1000ms)
	private static final String KEY_ROLES = "roles;";
	private final MemberService memberService;
	@Value("${spring.jwt.secret}")
	private String secretKey;

	// 토큰 생성
	public String generateToken(String username, List<String> roles) {
		Claims claims = Jwts.claims().setSubject(username);
		claims.put(KEY_ROLES, roles);

		var now = new Date();
		var expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME); // 토큰 생성으로부터 1시간까지 유효
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now) // 생성시간
			.setExpiration(expiredDate)
			.signWith(SignatureAlgorithm.HS512, this.secretKey) // 시그니처알고리즘(사용할 암호화 알고리즘), 비밀키
			.compact();
	}

	// jwt토큰으로부터 인증정보를 가져오는 메서드
	public Authentication getAuthentication(String jwt) {
		UserDetails userDetails = memberService.loadUserByUsername(getUsername(jwt));

		// 사용자 정보와 사용자 권한정보 포함한 토큰 반환
		return new UsernamePasswordAuthenticationToken(userDetails, "",
			userDetails.getAuthorities());
	}

	// 토큰에서 username 얻는 메서드
	public String getUsername(String token) {
		return parseClaims(token).getSubject(); // 토큰 생성 시 setSubject로 넣었던 username을 얻음
	}

	public boolean validateToken(String token) {
		if (!StringUtils.hasText(token)) return false;

		var claims = parseClaims(token);
		return !claims.getExpiration().before(new Date()); // 토큰 만료시간이 현재시간 이전인지 체크
	}

	// 토큰 유효성 확인
	private Claims parseClaims(String token) {
		try {
			return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}
}
