package com.kh.bclass.comment.model.service;

import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kh.bclass.board.model.service.BoardService;
import com.kh.bclass.board.model.vo.Board;
import com.kh.bclass.comment.model.dao.CommentMapper;
import com.kh.bclass.comment.model.vo.Comment;
import com.kh.bclass.exception.TokenSubjectMismatchException;
import com.kh.bclass.member.model.vo.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
	
	private final CommentMapper mapper;
	private final BoardService boardService;
	
    private void validateCommentWriter(String commentWriter, String username) {
        if (commentWriter == null || !commentWriter.equals(username)) {
            throw new TokenSubjectMismatchException("왜 요기서 예외가 발생함?");
        }
    }

	@Override
	public List<Comment> findByBoardNo(Long boardNo) {
		boardService.findById(boardNo);
		return mapper.findByBoardNo(boardNo);
	}

	@Override
	public void insertComment(Comment comment) {
		CustomUserDetails user = boardService.getAuthenticatedUser();
		log.info("{} / {}", comment.getUserNo(), user.getUsername());
		validateCommentWriter(comment.getUserNo(), user.getUsername());
		comment.setUserNo(String.valueOf(user.getUserNo()));
		
		mapper.insertComment(comment);
	}

}
