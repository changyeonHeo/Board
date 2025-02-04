package com.example.demo.service;

import com.example.demo.domain.CommentEntity;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 작성
    public CommentEntity createComment(Long bnum, String content) {
        CommentEntity comment = new CommentEntity();
        comment.setBnum(bnum); // 게시글 번호 설정
        comment.setContent(content); // 댓글 내용 설정
        return commentRepository.save(comment); // 저장
    }

    // 대댓글 작성
    public CommentEntity createReply(Long parentId, String content) {
        CommentEntity parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));

        CommentEntity reply = new CommentEntity();
        reply.setParent(parentComment); // 부모 댓글 설정
        reply.setBnum(parentComment.getBnum()); // 부모 댓글의 게시글 번호 설정
        reply.setContent(content); // 대댓글 내용 설정
        return commentRepository.save(reply); // 저장
    }

    // 게시글에 해당하는 댓글 및 대댓글 조회
    public List<CommentEntity> getCommentsByBnum(Long bnum) {
        return commentRepository.findByBnumAndParentIsNullOrderByCreatedDateDesc(bnum);
    }
}
