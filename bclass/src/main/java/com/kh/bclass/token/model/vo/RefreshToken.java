package com.kh.bclass.token.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshToken {
    private Long id;
    private String token;
    private Long expiration;
    private Long userNo; // 사용자 No
}