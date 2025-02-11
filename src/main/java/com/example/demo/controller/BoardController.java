package com.example.demo.controller;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.demo.domain.BoardEntity;
import com.example.demo.domain.CommentEntity;
import com.example.demo.dto.BoardRequest;
import com.example.demo.service.BoardService;
import com.example.demo.service.CommentService;

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
    private final CommentService commentService;

    @GetMapping("/")
    public String boardList(Model model, 
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "5") int size) {
        if (page < 1) return "redirect:/?page=1"; // ✅ 최소값 보장

        // ✅ 페이징 설정
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "bnum"));
        
        // ✅ 페이징된 게시글 가져오기
        Page<BoardEntity> boardPage = boardService.getBoardList(pageable);

        // ✅ LocalDateTime → Date 변환
        List<BoardEntity> formattedBoards = boardPage.getContent().stream()
                .map(board -> {
                    board.setFormattedDate(Date.from(board.getCreatedDate().atZone(ZoneId.systemDefault()).toInstant()));
                    return board;
                })
                .collect(Collectors.toList());

        model.addAttribute("boards", formattedBoards);
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

    @GetMapping("/board/{id}")
    public String boardDetail(@PathVariable Long id, Model model) {
    	BoardEntity currentPost = boardService.getBoardById(id);
        model.addAttribute("board", currentPost);

        // ✅ 이전글 & 다음글 가져오기
        BoardEntity prevPost = boardService.getPreviousPost(id);
        BoardEntity nextPost = boardService.getNextPost(id);
        model.addAttribute("prevPost", prevPost);
        model.addAttribute("nextPost", nextPost);

        // ✅ 댓글 리스트 가져오기
        List<CommentEntity> comments = commentService.getCommentsByBnum(id);
        model.addAttribute("comments", comments);
        
        // 🔥 콘솔 로그 (디버깅용)
        System.out.println("📌 게시글 ID: " + id);
        System.out.println("📌 댓글 개수: " + comments.size());
        for (CommentEntity comment : comments) {
            System.out.println("📌 Comment ID: " + comment.getId() + ", isDeleted: " + comment.getIsDeleted());
        }

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
    
 // ✅ 게시글 수정 페이지 이동
    @GetMapping("/board/edit/{id}")
    public String editBoardPage(@PathVariable Long id, Model model) {
        BoardEntity board = boardService.getBoardById(id);
        model.addAttribute("board", board);
        return "board/board_write";
    }

 // ✅ 기존 글 수정 API (PUT 요청)
    @PutMapping("/api/board/{bnum}")
    @ResponseBody
    public ResponseEntity<String> updateBoard(@PathVariable Long bnum, @RequestBody BoardRequest request) {
        boardService.updateBoard(bnum, request.getTitle(), request.getContent());
        return ResponseEntity.ok("글이 성공적으로 수정되었습니다.");
    }


    // ✅ 게시글 삭제 API (DELETE 요청)
    @DeleteMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        boardService.deleteBoard(id, currentUsername);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
