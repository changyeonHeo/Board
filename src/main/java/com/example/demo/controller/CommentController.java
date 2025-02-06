package com.example.demo.controller;

import com.example.demo.domain.CommentEntity;
import com.example.demo.dto.CommentRequest;
import com.example.demo.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    // ✅ 댓글 작성 (로그인한 사용자 자동 설정)
    @PostMapping
    public CommentEntity createComment(@RequestBody CommentRequest request) {
        if (request.getBnum() == null) {
            throw new IllegalArgumentException("bnum 값이 없습니다.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // ✅ 현재 로그인한 사용자

        return commentService.createComment(request.getBnum(), request.getContent(), currentUsername);
    }


    // ✅ 대댓글 작성
    @PostMapping("/{commentId}/reply")
    public CommentEntity createReply(@PathVariable Long commentId, @RequestBody CommentRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName(); // ✅ 현재 로그인한 사용자

        return commentService.createReply(commentId, request.getContent(), currentUsername);
    }

    // ✅ 댓글 및 대댓글 조회
    @GetMapping
    public List<CommentEntity> getComments(@RequestParam Long bnum) {
        return commentService.getCommentsByBnum(bnum);
    }
}
