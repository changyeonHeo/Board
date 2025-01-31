package com.example.demo.service;

import com.example.demo.domain.BoardEntity;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    	return boardRepository.findAllByOrderByBnumDesc();
    }

    public BoardEntity getBoardById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
    }
}
