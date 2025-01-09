package com.kh.bclass.board.model.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.bclass.board.model.dao.BoardMapper;
import com.kh.bclass.board.model.vo.Board;
import com.kh.bclass.exception.CustomAuthenticationException;
import com.kh.bclass.exception.ResourceNotFoundException;
import com.kh.bclass.exception.TokenSubjectMismatchException;
import com.kh.bclass.member.model.vo.CustomUserDetails;
import com.kh.bclass.storage.model.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {
	
	private final BoardMapper mapper;
	private final StorageService s3Service;
	
    public CustomUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomAuthenticationException("인증된 사용자가 아닙니다.");
        }
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
    
    private Board getBoardOrThrow(Long boardNo) {
        Board board = mapper.findById(boardNo);
        if (board == null) {
            throw new ResourceNotFoundException("게시글을 찾을 수 없습니다.");
        }
        return board;
    }
    
    private void deleteExistingFile(String fileUrl) {
        if (fileUrl != null) {
            s3Service.delete(fileUrl);
        }
    }

	@Override
	public void save(Board board, MultipartFile file) {
		
        CustomUserDetails user = getAuthenticatedUser();
        validateBoardWriter(board.getBoardWriter(), user.getUsername());
        
        if (file != null && !file.isEmpty()) {
            String fileUrl = s3Service.upload(file);
            board.setFileUrl(fileUrl);
        } else {
        	board.setFileUrl(null);
        }
        
        board.setBoardWriter(String.valueOf(user.getUserNo()));
        
		mapper.save(board);
	}

	@Override
	public List<Board> findAll(int page) {
		int size = 3;
		RowBounds rowBounds = new RowBounds(page * size, size);
		return mapper.findAll(rowBounds);
	}

	@Override
	public Board findById(Long boardNo) {
		Board board = mapper.findById(boardNo);
		if(board == null) {
			throw new RuntimeException("존재하지 않는 게시글");
		}
		return board;
	}

	@Override
	public String upfile(MultipartFile file) {
		return s3Service.upload(file);
	}

	@Override
	public void deleteFile(String fileUri) {
		s3Service.delete(fileUri);
	}

	@Override
    public Board updateBoard(Board board, MultipartFile file) {
        Board existingBoard = getBoardOrThrow(board.getBoardNo());

        existingBoard.setBoardTitle(board.getBoardTitle());
        existingBoard.setBoardContent(board.getBoardContent());

        if (file != null && !file.isEmpty()) {
    		deleteExistingFile(existingBoard.getFileUrl());
            String fileUrl = upfile(file);
            existingBoard.setFileUrl(fileUrl);
        }

        mapper.updateBoard(existingBoard);
        return existingBoard;
    }

	@Override
	public void deleteById(Long boardNo) {
        Board existingBoard = getBoardOrThrow(boardNo);
        CustomUserDetails user = getAuthenticatedUser();
        validateBoardWriter(user.getUsername(), existingBoard.getBoardWriter());
        
        mapper.deleteById(boardNo);
    	deleteExistingFile(existingBoard.getFileUrl());
	}

}
