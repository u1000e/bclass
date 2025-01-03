package com.kh.bclass.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.bclass.member.model.service.AuthenticateService;
import com.kh.bclass.member.model.service.MemberService;
import com.kh.bclass.member.model.vo.Member;
import com.kh.bclass.token.model.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
    private final TokenService tokenService;
    private final AuthenticateService authenticationService;
	
	@PostMapping("/login")
	public ResponseEntity<Map> login(@Valid @RequestBody Member member){
		Map<String, String> tokens = authenticationService.login(member.getUserName(), member.getUserPwd());
		return ResponseEntity.ok(tokens);
	}
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@Valid @RequestBody Member member){
		memberService.join(member.getUserName(), member.getUserPwd());
		return new ResponseEntity("회원가입에 성공했습니다.", HttpStatus.CREATED);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<Map> refresh(@RequestBody Map<String, String> request){
		String refreshToken = request.get("refreshToken");
		Map<String, String> tokens = tokenService.refreshAccessToken(refreshToken);
		return ResponseEntity.ok(tokens);
	}
	
}
