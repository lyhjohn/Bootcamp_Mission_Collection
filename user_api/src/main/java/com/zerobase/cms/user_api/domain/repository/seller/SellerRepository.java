package com.zerobase.cms.user_api.domain.repository.seller;

import com.zerobase.cms.user_api.domain.model.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

	Optional<Seller> findByEmail(String email);
	Optional<Seller> findByEmailAndPasswordAndVerifyIsTrue(String email, String password);

	boolean existsByEmail(String email);
}
