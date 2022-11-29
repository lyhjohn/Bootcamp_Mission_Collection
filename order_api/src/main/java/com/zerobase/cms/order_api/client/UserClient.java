package com.zerobase.cms.order_api.client;

import com.zerobase.cms.order_api.client.user.ChangeBalanceForm;
import com.zerobase.cms.order_api.client.user.CustomerDto;
import com.zerobasedomain.domain.common.UserVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

// FeignCLient 는 자동 Bean 등록됨
@FeignClient(name = "user-api", url = "${feign.client.url.user-api}")
public interface UserClient {

    @GetMapping("/customer/getInfo")
    ResponseEntity<CustomerDto> getInfo(@RequestHeader(name = "X_AUTH_TOKEN") String token);

    @PostMapping("/customer/balance")
    ResponseEntity<Integer> changeBalance(@RequestHeader(name = "X_AUTH_TOKEN") String token,
                                                 @RequestBody ChangeBalanceForm form);


}
