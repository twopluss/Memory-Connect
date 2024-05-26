package com.study.basicboard.repository;

import com.study.basicboard.domain.entity.Board;
import com.study.basicboard.domain.entity.UploadImage;
import com.study.basicboard.domain.enum_class.BoardCategory;
import com.study.basicboard.domain.enum_class.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByCategoryAndUserUserRoleNot(BoardCategory category, UserRole userRole, PageRequest pageRequest);
    Page<Board> findAllByCategoryAndTitleContainsAndUserUserRoleNot(BoardCategory category, String title, UserRole userRole, PageRequest pageRequest);
    // 데이터베이스에서 제목을 가지고 있는 게시물 들만 가져와라
    Page<Board> findAllByCategoryAndUserNicknameContainsAndUserUserRoleNot(BoardCategory category, String nickname, UserRole userRole, PageRequest pageRequest);

    Page<Board> findAllByCategoryAndYearsContainsAndUserUserRoleNot(BoardCategory category, String years, UserRole userRole, PageRequest pageRequest);
    List<Board> findAllByUserLoginId(String loginId);
    List<Board> findAllByCategoryAndUserUserRole(BoardCategory category, UserRole userRole);
    Long countAllByUserUserRole(UserRole userRole);
    Long countAllByCategoryAndUserUserRoleNot(BoardCategory category, UserRole userRole);


    //이미지 전시 관련 메소드




}
