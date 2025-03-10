package com.study.basicboard.repository;

import com.study.basicboard.domain.entity.UploadImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadImageRepository extends JpaRepository<UploadImage, Long> {

}
