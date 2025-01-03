
package com.kh.bclass.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.kh.bclass.member.model.vo.Member;

@Mapper
public interface MemberMapper {
	
	Member getUserName(String userName);

	void save(Member member);

}
