package com.kh.bclass.member.model.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.bclass.exception.DuplicateUserException;
import com.kh.bclass.member.model.dao.MemberMapper;
import com.kh.bclass.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
	
    private final MemberMapper mapper;
    private final PasswordEncoder passwordEncoder;

	public void join(String userName, String userPwd) {
		
		Member user = getUserByUsername(userName);
		
		if(user != null) {
			throw new DuplicateUserException("이미 존재하는 사용자 입니다.");
		}
		
        // 사용자 정보 저장
        Member member = Member.builder()
        				      .userName(userName)
        				      .userPwd(passwordEncoder.encode(userPwd))
        				      .role("ROLE_USER")
        				      .build();
        mapper.save(member);
        log.info("사용자 등록 성공 : {}", member);
	}
	
	public Member getUserByUsername(String userName) {
		return mapper.getUserName(userName);
	}
	

}
