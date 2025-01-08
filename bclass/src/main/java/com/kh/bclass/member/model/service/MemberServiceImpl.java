package com.kh.bclass.member.model.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.password.CompromisedPasswordException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kh.bclass.exception.DuplicateUserException;
import com.kh.bclass.member.model.dao.MemberMapper;
import com.kh.bclass.member.model.vo.CustomUserDetails;
import com.kh.bclass.member.model.vo.Member;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
	
    private final MemberMapper mapper;
    private final PasswordEncoder passwordEncoder;
    
    public void changePassword(String currentPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails)authentication.getPrincipal();
        
        if(!(passwordEncoder.matches(currentPassword, user.getPassword()))) {
        	throw new CompromisedPasswordException("비밀번호 변경에 실패했습니다.");
        }
        
        String encodedPassword = passwordEncoder.encode(newPassword);
        Map<String, String> map = new HashMap();
        map.put("username", user.getUsername());
        map.put("password", encodedPassword);
        mapper.changePassword(map);
    }

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

	@Override
	public void deleteAccount(@NotBlank(message = "비밀번호를 입력해주세요") String password) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
        	log.info("입력한거 : {}, userDetails : {}", password, userDetails.getPassword());
            throw new CompromisedPasswordException("비밀번호가 일치하지 않습니다.");
        }
        
		mapper.deleteAccount(userDetails);
	}
	

}
