package com.kh.bclass.token.model.service;

import java.util.Map;

public interface TokenService {
	
	Map<String, String> generateTokens(String userName, Long userNo);
	
	Map<String, String> refreshAccessToken(String refreshToken);

}
