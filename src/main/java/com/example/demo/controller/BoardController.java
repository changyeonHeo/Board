package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.BoardEntity;
import com.example.demo.dto.BoardRequest;
import com.example.demo.service.BoardService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/")
    public String boardList(Model model) {
        List<BoardEntity> boards = boardService.getAllBoards();

        // ✅ LocalDateTime → java.util.Date 변환
        List<BoardEntity> formattedBoards = boards.stream()
                .map(board -> {
                    board.setFormattedDate(Date.from(board.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()));
                    return board;
                })
                .collect(Collectors.toList());

        model.addAttribute("boards", formattedBoards);
        return "main"; // 메인 페이지로 이동
    }

    // ✅ 게시글 작성 페이지 이동
    @GetMapping("/board/write")
    public String writePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("writer", currentUsername); // JSP에서 사용 가능하도록 전달
        return "board/board_write";
    }

    // ✅ 게시글 저장 (RESTful API)
    @PostMapping("/api/board")
    @ResponseBody
    public ResponseEntity<String> saveBoard(@RequestBody BoardRequest request) {
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("제목을 입력하세요.");
        }
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("내용을 입력하세요.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // ✅ BoardService를 사용하여 저장
        boardService.saveBoard(request.getTitle(), request.getContent(), currentUsername, request.getBimage());

        return ResponseEntity.ok("글이 성공적으로 저장되었습니다.");
    }

    // ✅ 게시글 상세보기 페이지 이동
    @GetMapping("/board/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardEntity board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/board_detail"; // 게시글 상세보기 JSP로 이동
    }

    // ✅ 이미지 업로드 API (서머노트 이미지 업로드 처리)
    @PostMapping("/api/uploadImage")
    @ResponseBody
    public Map<String, String> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = "uploads/" + fileName;

            File destination = new File(filePath);
            file.transferTo(destination);

            response.put("imageUrl", "/" + filePath); // 클라이언트가 접근할 수 있도록 URL 반환
        } catch (IOException e) {
            response.put("error", "파일 업로드 실패");
        }
        return response;
    }
}
