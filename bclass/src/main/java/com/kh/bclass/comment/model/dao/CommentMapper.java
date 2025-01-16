package com.kh.bclass.comment.model.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.kh.bclass.comment.model.vo.Comment;

@Mapper
public interface CommentMapper {
	
	List<Comment> findByBoardNo(Long boardNo);
	
	void insertComment(Comment comment);

}
