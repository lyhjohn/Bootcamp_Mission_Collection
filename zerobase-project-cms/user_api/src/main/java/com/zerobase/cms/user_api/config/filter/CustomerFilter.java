package com.zerobase.cms.user_api.config.filter;

import com.zerobase.cms.user_api.exception.CustomException;
import com.zerobase.cms.user_api.exception.ErrorCode;
import com.zerobase.cms.user_api.service.customer.CustomerService;
import com.zerobasedomain.config.JwtAuthenticationProvider;
import com.zerobasedomain.domain.common.UserType;
import com.zerobasedomain.domain.common.UserVo;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import static com.zerobase.cms.user_api.exception.ErrorCode.NOT_FOUND_USER;
import static com.zerobasedomain.domain.common.UserType.CUSTOMER;

@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
public class CustomerFilter implements Filter {

	private final JwtAuthenticationProvider provider;
	private final CustomerService customerService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		System.out.println("토큰 만료 검증");
		HttpServletRequest req = (HttpServletRequest) request;
		String token = req.getHeader("X_AUTH_TOKEN");
		System.out.println("token = " + token);
		if (!provider.validateToken(token)) {
			throw new ServletException("Invalid Access");
		}

		UserVo vo = provider.getUserVo(token);
		customerService.findByIdAndEmail(vo.getId(), vo.getEmail());

		if (!provider.getRoles(token).equals(CUSTOMER.toString())) {
			throw new CustomException(NOT_FOUND_USER);
		}

		chain.doFilter(request, response);
	}
}
