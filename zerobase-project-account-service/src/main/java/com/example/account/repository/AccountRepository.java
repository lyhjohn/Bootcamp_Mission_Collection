package com.example.account.repository;

import com.example.account.domain.Account;

import java.util.List;
import java.util.Optional;

import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
	Optional<Account> findFirstByOrderByIdDesc(); // id로 내림차순 정렬해서 첫번째 값을 가져온다.
	// Optional : 값이 있으면 가져오고 없으면 안가져옴
	Integer countByAccountUser(AccountUser accountUser);
	Optional<Account> findByAccountNumber(String AccountNumber); // Account에서 AccountNumber찾아서 그에 맞는 Account 정보 줌

	List<Account> findByAccountUser(AccountUser accountUser);
}
