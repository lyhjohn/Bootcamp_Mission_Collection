package com.zerobase.userapi.config;

import com.zerobasedomain.config.JwtAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

	@Bean
	public JwtAuthenticationProvider jwtAuthenticationProvider(){
		return new JwtAuthenticationProvider();
	}
}
