package com.example.demo.service;

import com.example.demo.domain.BoardEntity;
import com.example.demo.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public void saveBoard(String title, String content, String writer, MultipartFile file) throws IOException {
        BoardEntity board = new BoardEntity();
        board.setTitle(title);
        board.setContent(content);
        board.setWriter(writer);

        // 이미지 파일 처리
        if (file != null && !file.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename(); // 고유 파일명 생성
            String filePath = "uploads/" + fileName; // 파일 저장 경로
            File destination = new File(filePath);

            // 디렉토리 생성
            if (!destination.getParentFile().exists()) {
                destination.getParentFile().mkdirs();
            }

            file.transferTo(destination); // 파일 저장
            board.setBimage(filePath); // DB에 저장될 경로 설정
        }

        boardRepository.save(board); // DB 저장
    }
}
