package com.example.demo.service;

import com.example.demo.domain.CommentEntity;
import com.example.demo.repository.CommentRepository;
import jakarta.transaction.Transactional;
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
                .writer(writer)
                .createdDate(LocalDateTime.now())
                .isDeleted(false)  // 🔥 기본값 false
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
                .writer(writer)
                .createdDate(LocalDateTime.now())
                .parent(parentComment)
                .isDeleted(false)  // 🔥 기본값 false
                .build();
        return commentRepository.save(reply);
    }

    // ✅ 게시글의 댓글 및 대댓글 조회
    public List<CommentEntity> getCommentsByBnum(Long bnum) {
        List<CommentEntity> comments = commentRepository.findByBnumAndParentIsNullOrderByCreatedDateDesc(bnum);

        // ✅ `null` 값 체크 후 기본값 설정
        for (CommentEntity comment : comments) {
            if (Boolean.TRUE.equals(comment.getIsDeleted())) {  
                comment.setIsDeleted(false);
            }
        }
        return comments;
    }

    // ✅ 댓글 삭제 (대댓글 포함)
    @Transactional
    public void deleteComment(Long commentId) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        if (comment.getParent() == null) { 
            // ✅ 부모 댓글 삭제 시, 대댓글도 함께 삭제
            List<CommentEntity> replies = comment.getReplies();
            for (CommentEntity reply : replies) {
                reply.setIsDeleted(true);  // 🔥 논리적 삭제
                reply.setContent("삭제된 댓글입니다.");
                commentRepository.save(reply);
            }
            commentRepository.delete(comment);
        } else {
            // ✅ 대댓글 삭제 시 "삭제된 댓글입니다"로 변경
            comment.setIsDeleted(true);  // 🔥 논리적 삭제
            comment.setContent("삭제된 댓글입니다.");
            commentRepository.save(comment);
        }
    }
}
