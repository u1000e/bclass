package com.kh.bclass.member.model.service;

import java.util.Map;

public interface AuthenticateService {
	
	Map<String, String> login(String userName, String password);

}
