package com.zerobasedomain.domain.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserType {

	CUSTOMER("고객"),
	SELLER("판매자");

	private final String type;
}
