package zerobase.stock.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.stock.exception.impl.AlreadyExistUserException;
import zerobase.stock.model.Auth.SignIn;
import zerobase.stock.model.Auth.SignUp;
import zerobase.stock.persist.entity.MemberEntity;
import zerobase.stock.persist.MemberRepository;

@Slf4j
@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

	// AppConfig 파일에서 빈 등록했음
	private final PasswordEncoder passwordEncoder;
	private final MemberRepository memberRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return memberRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("NOT_FOUND_USER -> " + username));
	}

	public MemberEntity register(SignUp member) {
		boolean isExists = memberRepository.existsByUsername(member.getUsername());
		if (isExists) {
			throw new AlreadyExistUserException();
		}

		member.setPassword(passwordEncoder.encode(member.getPassword()));
		var result = memberRepository.save(member.toEntity());
		return result;
	}

	public MemberEntity authenticate(SignIn member) {
		var user = memberRepository.findByUsername(member.getUsername())
			.orElseThrow(() -> new RuntimeException("존재하지 않는 ID 입니다."));
		// 인코딩 된 user의 pw와 입력받은 날것의 pw 비교
		if (passwordEncoder.matches(member.getPassword(), user.getPassword())) {
			throw new RuntimeException("비밀번호가 일치하지 않습니다.");
		}
		return user;
	}
}
