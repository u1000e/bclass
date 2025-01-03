package com.kh.bclass.member.model.service;

import com.kh.bclass.member.model.vo.Member;

public interface MemberService {


	void join(String userName, String userPwd);

	Member getUserByUsername(String username);

}
