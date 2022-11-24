package com.zerobase.cms.user_api.service.seller;

import static com.zerobase.cms.user_api.exception.ErrorCode.ALREADY_VERIFY;
import static com.zerobase.cms.user_api.exception.ErrorCode.EXPIRE_CODE;
import static com.zerobase.cms.user_api.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobase.cms.user_api.exception.ErrorCode.WRONG_VERIFICATION_CODE;

import com.zerobase.cms.user_api.domain.SignUpForm;
import com.zerobase.cms.user_api.domain.model.Seller;
import com.zerobase.cms.user_api.domain.repository.seller.SellerRepository;
import com.zerobase.cms.user_api.exception.CustomException;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SellerService {

	private final SellerRepository sellerRepository;

	public Seller findByIdAndEmail(Long id, String email) {
		return sellerRepository.findById(id).stream()
			.filter(x -> x.getEmail().equals(email)).findFirst()
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	public Seller findValidSeller(String email, String password) {
		return sellerRepository.findByEmailAndPasswordAndVerifyIsTrue(email, password)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
	}

	public Seller signUp(SignUpForm form) {
		return sellerRepository.save(Seller.from(form));
	}

	public boolean isEmailExist(String email) {
		return sellerRepository.existsByEmail(email);
	}

	public void changeSellerValidateEmail(Long sellerId, String verificationCode) {
		Seller seller = sellerRepository.findById(sellerId)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));

		seller.setVerificationCode(verificationCode);
		seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
	}

	public void verifyEmail(String email, String code) {
		Seller seller = sellerRepository.findByEmail(email)
			.orElseThrow(() -> new CustomException(NOT_FOUND_USER));
		if (seller.isVerify()) {
			throw new CustomException(ALREADY_VERIFY);
		}
		if (!seller.getVerificationCode().equals(code)) {
			throw new CustomException(WRONG_VERIFICATION_CODE);
		}
		if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
			throw new CustomException(EXPIRE_CODE);
		}
		seller.setVerify(true);
	}

}
