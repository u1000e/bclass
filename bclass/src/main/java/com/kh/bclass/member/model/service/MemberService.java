package com.kh.bclass.member.model.service;

import com.kh.bclass.member.model.vo.Member;

import jakarta.validation.constraints.NotBlank;

public interface MemberService {


	void join(String userName, String userPwd);

	Member getUserByUsername(String username);
	
	void changePassword(String currentPassword, String newPassword);

	void deleteAccount(@NotBlank(message = "비밀번호를 입력해주세요") String password);

}
