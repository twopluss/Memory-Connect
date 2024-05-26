/*
package com.study.basicboard.service;

import com.study.basicboard.domain.entity.UploadYearImage;
import com.study.basicboard.repository.UploadImageYearRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import javax.transaction.Transactional;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadImageService {

    private static UploadImageYearRepository uploadImageYearRepository;
    private static final String rootPath = System.getProperty("user.dir");
    private static final String fileDir = rootPath + "/src/main/resources/static/upload-images/";

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public static UploadYearImage saveImage(MultipartFile multipartFile, String category, String years) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalYearFilename = multipartFile.getOriginalFilename();
        // 원본 파일명 -> 서버에 저장된 파일명 (중복 X)
        // 파일명이 중복되지 않도록 UUID로 설정 + 확장자 유지
        String savedYearFilename = UUID.randomUUID() + "." + extractExt(originalYearFilename);

        // 파일 저장
        try {
            Path path = Paths.get(fileDir, savedYearFilename);
            Files.createFile(path);
            multipartFile.transferTo(path);
        } catch (Exception e) {
            System.out.println("create file failed");
        }

        return uploadImageYearRepository.save(UploadYearImage.builder()
                .category(category)
                .originalYearFilename(originalYearFilename)
                .savedYearFilename(savedYearFilename)
                .years(years)
                .build());
    }

    @Transactional
    public void deleteImage(UploadYearImage uploadYearImage) throws IOException {
        uploadImageYearRepository.delete(uploadYearImage);
        Files.deleteIfExists(Paths.get(getFullPath(uploadYearImage.getSavedYearFilename())));
    }

    // 확장자 추출
    private static String extractExt(String originalYearFilename) {
        int pos = originalYearFilename.lastIndexOf(".");
        return originalYearFilename.substring(pos + 1);
    }


}
*/
