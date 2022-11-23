package com.zerobasedomain.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class Aes256UtilTest {

	@Test
	void encryptAndDecrypt() {
		String encrypt = Aes256Util.encrypt("Hello world");
		assertThat(Aes256Util.decrypt(encrypt)).isEqualTo("Hello world");
	}

}