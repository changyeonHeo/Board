package com.example.demo.service;

import com.example.demo.domain.BoardEntity;


import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public void saveBoard(String title, String content, String writer, String bimage) {
        BoardEntity board = BoardEntity.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .bimage(bimage) // ✅ bimage 추가 가능해짐
                .build();

        boardRepository.save(board);
    }
    
    public List<BoardEntity> getAllBoards() {
        return boardRepository.findAllByOrderByBnumDesc().stream()
                .map(board -> {
                    board.setFormattedDate(Date.from(board.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()));
                    return board;
                })
                .collect(Collectors.toList());
    }

    public BoardEntity getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }
    
 // ✅ 최신순 페이징된 게시글 조회
    public Page<BoardEntity> getBoardList(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    // ✅ 게시글 삭제 메서드
    @Transactional
    public void deleteBoard(Long id, String username) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (!board.getWriter().equals(username)) {
            throw new IllegalStateException("게시글 삭제 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
    
    public void updateBoard(Long bnum, String title, String content) {
        BoardEntity board = boardRepository.findById(bnum)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        board.setTitle(title);
        board.setContent(content);
        boardRepository.save(board); // 기존 게시글을 수정하여 저장
    }
    
}
