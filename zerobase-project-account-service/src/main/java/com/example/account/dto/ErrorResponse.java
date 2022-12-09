package com.example.account.dto;

import com.example.account.type.ErrorCode;
import lombok.*;
import org.springframework.stereotype.Service;

@Getter
@Service
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;

}
