package com.example.demo.service;

import com.example.demo.domain.CommentEntity;
import com.example.demo.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    // ✅ 댓글 작성
    public CommentEntity createComment(Long bnum, String content, String writer) {
        CommentEntity comment = CommentEntity.builder()
                .bnum(bnum)
                .content(content)
                .writer(writer) // ✅ 작성자 저장
                .createdDate(LocalDateTime.now())
                .build();

        return commentRepository.save(comment);
    }

    // ✅ 대댓글 작성
    public CommentEntity createReply(Long parentId, String content, String writer) {
        CommentEntity parentComment = commentRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        CommentEntity reply = CommentEntity.builder()
                .bnum(parentComment.getBnum())
                .content(content)
                .writer(writer) // ✅ 작성자 저장
                .createdDate(LocalDateTime.now())
                .parent(parentComment) // ✅ 부모 댓글 설정
                .build();

        return commentRepository.save(reply);
    }

    // ✅ 게시글의 댓글 및 대댓글 조회
    public List<CommentEntity> getCommentsByBnum(Long bnum) {
        return commentRepository.findByBnumAndParentIsNullOrderByCreatedDateDesc(bnum);
    }
}
