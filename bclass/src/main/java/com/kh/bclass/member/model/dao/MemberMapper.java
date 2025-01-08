
package com.kh.bclass.member.model.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.kh.bclass.member.model.vo.CustomUserDetails;
import com.kh.bclass.member.model.vo.Member;

@Mapper
public interface MemberMapper {
	
	Member getUserName(String userName);

	void save(Member member);

	void changePassword(Map<String, String> map);

	void deleteAccount(CustomUserDetails userDetails);

}
