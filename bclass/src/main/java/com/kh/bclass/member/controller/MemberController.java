package com.kh.bclass.member.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.bclass.member.model.service.AuthenticateService;
import com.kh.bclass.member.model.service.MemberService;
import com.kh.bclass.member.model.vo.ChangePasswordRequest;
import com.kh.bclass.member.model.vo.LoginResponse;
import com.kh.bclass.member.model.vo.Member;
import com.kh.bclass.token.model.service.TokenService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody Member member){

		Map<String, String> tokens = authenticationService.login(member.getUserName(), member.getUserPwd());
		
		LoginResponse response = LoginResponse.builder()	
											  .username(member.getUserName())
											  .accessToken(tokens.get("accessToken"))
											  .refreshToken(tokens.get("refreshToken"))
											  .build();
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@Valid @RequestBody Member member){
		log.info("내가받음?");
		memberService.join(member.getUserName(), member.getUserPwd());
		return new ResponseEntity("회원가입에 성공했습니다.", HttpStatus.CREATED);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<Map> refresh(@RequestBody Map<String, String> request){
		String refreshToken = request.get("refreshToken");
		Map<String, String> tokens = tokenService.refreshAccessToken(refreshToken);
		return ResponseEntity.ok(tokens);
	}
	
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String refreshToken){
        if (refreshToken != null && !refreshToken.isEmpty()) {
            tokenService.deleteRefreshToken(refreshToken);
            return ResponseEntity.ok("로그아웃이 완료되었습니다.");
        }
        return ResponseEntity.badRequest().body("유효하지 않은 토큰입니다.");
    }
     
    @PutMapping
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest request){
    	memberService.changePassword(request.getCurrentPassword(), request.getNewPassword());
    	return ResponseEntity.status(HttpStatus.CREATED).body("비밀번호 변경에 성공했습니다~");
    }
    
    @DeleteMapping
    public ResponseEntity<?> deleteAccount(@RequestBody Map<String, String> password){
    	memberService.deleteAccount(password.get("password"));
    	return ResponseEntity.ok("계정이 삭제되었습니다.");
    }
	
    
}
