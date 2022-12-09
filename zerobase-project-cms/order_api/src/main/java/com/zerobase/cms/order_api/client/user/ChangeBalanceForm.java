package com.zerobase.cms.order_api.client.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChangeBalanceForm {

	private String from;
	private String message;
	private Integer money;
}
