package com.example.demo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.BoardEntity;
import com.example.demo.dto.BoardRequest;
import com.example.demo.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardRepository boardRepository;

    // 게시글 목록 페이지로 이동
    @GetMapping
    public String boardList(Model model) {
        List<BoardEntity> boards = boardRepository.findAll(); // 모든 게시글 가져오기
        model.addAttribute("boards", boards); // JSP에서 사용 가능하도록 전달
        return "board/board_list"; // 게시글 목록 JSP로 이동
    }

    // 게시글 작성 페이지로 이동
    @GetMapping("/write")
    public String writePage(Model model) {
        // 현재 로그인된 사용자 이름 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("writer", currentUsername); // JSP에서 사용 가능하도록 전달
        return "board/board_write";
    }

    // 게시글 저장 (RESTful API)
    @PostMapping("/api")
    @ResponseBody
    public String saveBoard(@RequestBody BoardRequest request) {
        // 로그인된 사용자 정보를 자동으로 작성자로 설정
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        BoardEntity board = BoardEntity.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .writer(currentUsername) // 작성자 저장
                .bimage(request.getBimage()) // 이미지 저장
                .build();
        boardRepository.save(board);
        return "글이 성공적으로 저장되었습니다.";
    }

    // 게시글 상세보기 페이지로 이동
    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardEntity board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));
        model.addAttribute("board", board); // JSP에서 사용 가능하도록 전달
        return "board/board_detail"; // 게시글 상세보기 JSP로 이동
    }
}
