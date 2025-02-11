package com.example.demo.controller;

import com.example.demo.domain.CommentEntity;
import com.example.demo.dto.CommentRequest;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // ✅ 댓글 작성
    @PostMapping
    public CommentEntity createComment(@RequestBody CommentRequest request) {
        if (request.getBnum() == null) {
            throw new IllegalArgumentException("bnum 값이 없습니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        return commentService.createComment(request.getBnum(), request.getContent(), currentUsername);
    }

    // ✅ 대댓글 작성
    @PostMapping("/{commentId}/reply")
    public ResponseEntity<CommentEntity> createReply(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        CommentEntity newReply = commentService.createReply(commentId, request.getContent(), currentUsername);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(newReply); // ✅ 201 Created 응답 추가
    }



    // ✅ 특정 게시글의 댓글 조회 (JSON 반환)
    @GetMapping
    public List<CommentEntity> getComments(@RequestParam Long bnum) {
        List<CommentEntity> comments = commentService.getCommentsByBnum(bnum);
        return comments;
    }

    // ✅ 댓글 삭제 (대댓글 포함)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // ✅ 본인 댓글인지 확인 후 삭제
        commentService.deleteComment(commentId, currentUsername);
        return ResponseEntity.ok("댓글이 삭제되었습니다.");
    }

}
