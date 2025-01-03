package com.kh.bclass.member.model.service;

import java.util.Map;

import com.kh.bclass.member.model.vo.Member;

public interface MemberService {

	Map<String, String> login(String userName, String userPwd);

	void join(String userName, String userPwd);

	Map<String, String> refresh(String refreshToken);
	
	Member getUserByUsername(String username);

}
