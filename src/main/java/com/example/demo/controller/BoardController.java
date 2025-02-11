package com.example.demo.controller;

import org.springframework.data.domain.*;
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
        if (page < 1) return "redirect:/?page=1"; 

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "bnum"));
        Page<BoardEntity> boardPage = boardService.getBoardList(pageable);

        model.addAttribute("boards", boardPage.getContent());
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", boardPage.getTotalPages());

        return "main"; 
    }

    @GetMapping("/board/write")
    public String writePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        model.addAttribute("writer", currentUsername);
        return "board/board_write";
    }

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

        BoardEntity prevPost = boardService.getPreviousPost(id);
        BoardEntity nextPost = boardService.getNextPost(id);
        model.addAttribute("prevPost", prevPost);
        model.addAttribute("nextPost", nextPost);

        List<CommentEntity> comments = commentService.getCommentsByBnum(id);
        model.addAttribute("comments", comments);
        
        return "board/board_content";
    }

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

    @GetMapping("/board/edit/{id}")
    public String editBoardPage(@PathVariable Long id, Model model) {
        BoardEntity board = boardService.getBoardById(id);

        // 🔥 본인 글이 아니면 수정 페이지 접근 차단
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        if (!board.getWriter().equals(currentUsername)) {
            return "redirect:/?error=unauthorized";
        }

        model.addAttribute("board", board);
        return "board/board_write";
    }

    @PutMapping("/api/board/{bnum}")
    @ResponseBody
    public ResponseEntity<String> updateBoard(@PathVariable Long bnum, @RequestBody BoardRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 🔥 본인 확인 후 수정 가능하도록 처리
        BoardEntity board = boardService.getBoardById(bnum);
        if (!board.getWriter().equals(currentUsername)) {
            return ResponseEntity.status(403).body("본인만 수정할 수 있습니다.");
        }

        boardService.updateBoard(bnum, request.getTitle(), request.getContent());
        return ResponseEntity.ok("글이 성공적으로 수정되었습니다.");
    }

    @DeleteMapping("/api/board/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // 🔥 본인 확인 후 삭제 가능하도록 처리
        BoardEntity board = boardService.getBoardById(id);
        if (!board.getWriter().equals(currentUsername)) {
            return ResponseEntity.status(403).body("본인만 삭제할 수 있습니다.");
        }

        boardService.deleteBoard(id, currentUsername);
        return ResponseEntity.ok("게시글이 삭제되었습니다.");
    }
}
