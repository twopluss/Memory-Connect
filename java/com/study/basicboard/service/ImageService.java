/*
package com.study.basicboard.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class ImageService {

    // 이미지가 저장된 기본 경로를 지정합니다.
    private String basePath = "classpath:/static/";

    */
/**
     * 특정 카테고리와 연도에 해당하는 이미지 파일 경로를 리스트로 가져옵니다.
     * @param category 카테고리(clothes, food, movie)
     * @param year 연도(1960, 1980, 2000, 2020)
     * @return 이미지 파일 경로 리스트
     *//*

    public List<String> getImagesByCategoryAndYear(String category, String year) {
        List<String> imagePaths = new ArrayList<>();

        // 연도 및 카테고리에 해당하는 폴더 경로를 생성합니다.
        String folderPath = basePath + year + category + "/";

        // 폴더에서 이미지 파일 경로 목록을 가져옵니다.
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles((dir, name) -> name.endsWith(".jpg") || name.endsWith(".png") || name.endsWith(".jpeg"));

            if (files != null) {
                for (File file : files) {
                    // 이미지 파일 경로를 리스트에 추가합니다.
                    String relativePath = "/static/" + year + category + "/" + file.getName();
                    imagePaths.add(relativePath);
                }
            }
        }

        return imagePaths;
    }
}*/
