package com.kh.bclass.token.model.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.kh.bclass.token.model.vo.RefreshToken;

@Mapper
public interface RefreshTokenMapper {

    void save(RefreshToken refreshToken);

    RefreshToken findByToken(String token);

    @Delete("DELETE FROM refresh_token WHERE user_no = #{userNo}")
    void deleteByUserNo(Long userNo);
}
