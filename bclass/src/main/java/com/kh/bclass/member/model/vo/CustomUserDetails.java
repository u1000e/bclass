package com.kh.bclass.member.model.vo;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;


@Data
@AllArgsConstructor
@Builder
public class CustomUserDetails implements UserDetails{
	private String username;
	private String password;
	private Long userNo;
	private Collection<? extends GrantedAuthority> authorities;

}
