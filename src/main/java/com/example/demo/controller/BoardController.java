package com.example.demo.controller;

import org.springframework.data.domain.Page;
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

    // ✅ 게시글 목록 조회 (페이징 포함)
    @GetMapping("/")
    public String boardList(Model model, 
                            @RequestParam(defaultValue = "1") int page, 
                            @RequestParam(defaultValue = "5") int size) {
        if (page < 1) return "redirect:/?page=1"; // ✅ 최소값 보장

        Page<BoardEntity> boardPage = boardService.getBoardList(page, size);
        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardPage.getTotalPages());

        return "main"; // 메인 페이지로 이동
    }

    // ✅ 게시글 작성 페이지 이동
    @GetMapping("/board/write")
    public String writePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("writer", currentUsername);
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

        boardService.saveBoard(request.getTitle(), request.getContent(), currentUsername, request.getBimage());
        return ResponseEntity.ok("글이 성공적으로 저장되었습니다.");
    }

    // ✅ 게시글 상세보기
    @GetMapping("/board/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
        BoardEntity board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/board_content";
    }

    // ✅ 이미지 업로드
    @PostMapping("/api/uploadImage")
    @ResponseBody
    public Map<String, Object> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File uploadFile = new File(uploadDir + fileName);

            if (!uploadFile.getParentFile().exists()) {
                uploadFile.getParentFile().mkdirs();
            }

            file.transferTo(uploadFile);
            response.put("url", "/uploads/" + fileName);
            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "이미지 업로드 실패: " + e.getMessage());
        }
        return response;
    }
}
