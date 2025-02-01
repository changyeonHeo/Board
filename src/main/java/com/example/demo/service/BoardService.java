package com.example.demo.service;

import com.example.demo.domain.BoardEntity;

import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
    public Page<BoardEntity> getBoardList(int page, int size) {
        if (page < 1) page = 1; // ✅ 최소 페이지 값 보장
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "bnum"));
        return boardRepository.findAllByOrderByBnumDesc(pageable);
    }

}
