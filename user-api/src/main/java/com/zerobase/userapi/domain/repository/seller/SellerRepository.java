package com.zerobase.userapi.domain.repository.seller;

import com.zerobase.userapi.domain.model.Seller;
import java.util.Optional;
import org.bouncycastle.crypto.agreement.jpake.JPAKERound1Payload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

	Optional<Seller> findByEmail(String email);
	Optional<Seller> findByEmailAndPasswordAndVerifyIsTrue(String email, String password);

	boolean existsByEmail(String email);
}
