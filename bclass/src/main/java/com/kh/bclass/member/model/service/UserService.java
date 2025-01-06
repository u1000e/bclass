package com.kh.bclass.member.model.service;

import java.util.Collections;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kh.bclass.exception.CustomAuthenticationException;
import com.kh.bclass.member.model.dao.MemberMapper;
import com.kh.bclass.member.model.vo.CustomUserDetails;
import com.kh.bclass.member.model.vo.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
	
	private final MemberMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Member user = mapper.getUserName(username);
		
		if(user == null) {
			throw new CustomAuthenticationException("존재하지 않는 사용자입니다.");
		}
		
		return CustomUserDetails.builder()
								.username(user.getUserName())
								.password(user.getUserPwd())
								.userNo(user.getUserNo())
								.authorities(Collections.singletonList(new SimpleGrantedAuthority(user.getRole())))
								.build();
		
	}

}
