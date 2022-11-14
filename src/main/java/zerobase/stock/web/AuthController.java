package zerobase.stock.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.stock.model.Auth;
import zerobase.stock.security.TokenProvider;
import zerobase.stock.service.MemberService;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

	private final MemberService memberService;
	private final TokenProvider tokenProvider;

	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
		var result = memberService.register(request);
		// 회원가입
		return ResponseEntity.ok(result);
	}

	@PostMapping("/signin")
	public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
		var member = memberService.authenticate(request);

		// 비밀번호 일치하다는 검증 끝났으면 토큰 생성
		var token = tokenProvider.generateToken(member.getUsername(), member.getRoles());
		log.info("user login -> {}", request.getUsername());
		return ResponseEntity.ok(token);
	}
}
