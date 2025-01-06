package com.kh.bclass.board.model.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kh.bclass.board.model.dao.BoardMapper;
import com.kh.bclass.board.model.vo.Board;
import com.kh.bclass.exception.CustomAuthenticationException;
import com.kh.bclass.exception.TokenSubjectMismatchException;
import com.kh.bclass.member.model.vo.CustomUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper mapper;
	
    public CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Authentication이 null이 아니고 인증된 상태인지 확인
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomAuthenticationException("인증된 사용자가 아닙니다.");
        }
        
        // Principal이 CustomUserDetails인지 확인
        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new CustomAuthenticationException("유효하지 않은 사용자 정보입니다.");
        }
        
        return (CustomUserDetails)authentication.getPrincipal();
    }
    
    private void validateBoardWriter(String boardWriter, String username) {
        if (boardWriter != null && !boardWriter.equals(username)) {
            throw new TokenSubjectMismatchException("요청한 사용자와 게시글 작성자가 일치하지 않습니다.");
        }
    }

	@Override
	public void save(Board board) {
		
        CustomUserDetails user = getAuthenticatedUser();
        validateBoardWriter(board.getBoardWriter(), user.getUsername());
        
        board.setBoardWriter(String.valueOf(user.getUserNo()));
        
		mapper.save(board);
	}

	@Override
	public List<Board> findAll() {
		return mapper.findAll();
	}

	@Override
	public Board findById(Long boardNo) {
		Board board = mapper.findById(boardNo);
		if(board == null) {
			throw new RuntimeException("존재하지 않는 게시글");
		}
		return board;
	}

}
