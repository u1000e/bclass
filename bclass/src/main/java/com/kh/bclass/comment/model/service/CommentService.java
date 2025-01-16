package com.kh.bclass.comment.model.service;

import java.util.List;

import com.kh.bclass.comment.model.vo.Comment;

public interface CommentService {
	
	List<Comment> findByBoardNo(Long boardNo);
	
	void insertComment(Comment comment);

}
