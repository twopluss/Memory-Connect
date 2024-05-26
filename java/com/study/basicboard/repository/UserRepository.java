package com.study.basicboard.repository;

import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //해당 클래스를 스프링의 빈(Bean)으로 등록
public interface UserRepository extends JpaRepository<User, Long> {
    // 이곳에서 사용된 메소드는 jpa 라이브러리에서 사용하는 메소드이다 - 사용자 정의가 아님
    Optional<User> findByLoginId(String loginId);
    // Optional은 값이 있을 수도 있고 없을 수도 있는 값을 나타내는 컨테이너 객체
    Page<User> findAllByNicknameContains(String nickname, PageRequest pageRequest);
    // 닉네임에 특정 문자열이 포함된 사용자를 검색하고, 그 결과를 페이지 단위로 반환하는 역할 Spring Data에서 제공하는 페이징 처리를 위한 객체입니다. 데이터베이스에서 쿼리한 결과를 페이징하여 반환할 때 사용
    Boolean existsByLoginId(String loginId);
    Boolean existsByNickname(String nickname);
    Long countAllByUserRole(UserRole userRole);

}
