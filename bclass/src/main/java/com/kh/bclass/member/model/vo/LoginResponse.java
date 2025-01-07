package com.kh.bclass.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse {
	private String username;
	private String accessToken;
	private String refreshToken;

}
