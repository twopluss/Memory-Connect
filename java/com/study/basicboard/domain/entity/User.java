package com.study.basicboard.domain.entity;

import com.study.basicboard.domain.enum_class.UserRole;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // jpa 라이브러리로 db 테이블이라는 것을 인지 시키기 위한 어노테이션
@AllArgsConstructor // 롬복 라이브러리 필드에 있는 모든 코드의 생성자를 만든다
@NoArgsConstructor // 롬복 라이브러리로 기본 생성자를 생성한다
@Builder // 롬복 라이브러리로 더 높은 가독성을 위해 사용
@Getter
@Setter
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // jpa 라이브러리로 자동으로 프라이머리 키를 등록
    private Long id;

    private String loginId;     // 로그인할 때 사용하는 아이디
    private String password;    // 비밀번호
    private String nickname;    // 닉네임
    private LocalDateTime createdAt;    // 가입 시간
    private Integer receivedLikeCnt; // 유저가 받은 좋아요 개수 (본인 제외)

    @Enumerated(EnumType.STRING) // 열거형 상수를 데이터베이스에 문자열 형태로 저장하거나 검색할 때 사용
    private UserRole userRole;      // 권한

    @OneToMany(mappedBy = "user", orphanRemoval = true) // //orphanRemoval = true user가 지워지면 관계된 모든 엔티티 내용 지워짐 사용자가 글을 여러개 작성할수 있게 하나의user 엔티티가 여러개으 board 엔티랑 연관이 가능
    private List<Board> boards;     // 작성글

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Like> likes;       // 유저가 누른 좋아요

    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private List<Comment> comments; // 댓글

    //Authentication 객체는 현재 사용자의 인증 정보
    public void rankUp(UserRole userRole, Authentication auth) {
        this.userRole = userRole; // 현재 문자열의 상태

        // 현재 저장되어 있는 Authentication 수정 => 재로그인 하지 않아도 권한 수정 되기 위함
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(); // 업데이트된 권한 정보를 담을 updatedAuthorities 리스트를 생성
        updatedAuthorities.add(new SimpleGrantedAuthority(userRole.name())); // SimpleGrantedAuthority 객체를 생성하여 userRole을 기반으로한 권한 정보를 생성하고, 이를 updatedAuthorities 리스트에 추가
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities); //새로운 인증 정보인 newAuth를 생성합니다. 이 때, 사용자의 principal(주로 사용자의 식별 정보)과 credentials(주로 사용자의 비밀번호)는 기존의 인증 정보인 auth에서 가져오고, 업데이트된 권한 정보는 앞서 생성한 updatedAuthorities 리스트를 사용합니다.  새로운 인증 정보를 생성할 때에는 주로 UsernamePasswordAuthenticationToken 클래스를 사용합니다.
        SecurityContextHolder.getContext().setAuthentication(newAuth); //현재 스레드의 인증 정보를 업데이트합니다.

        //스레드(thread)는 컴퓨터 프로세스 내에서 실행되는 독립적인 실행
    }

    public void likeChange(Integer receivedLikeCnt) { // null 값을 허용
        this.receivedLikeCnt = receivedLikeCnt;
        if (this.receivedLikeCnt >= 10 && this.userRole.equals(UserRole.SILVER)) {
            this.userRole = UserRole.GOLD;
        }
    } // 좋아요 10개 이상이고 실버 등급이라면 골드 등급으로 업데이트 해주는 기능

    public void edit(String newPassword, String newNickname) {
        this.password = newPassword;
        this.nickname = newNickname;
    }

    public void changeRole() { // 이 메소드를 호출하여 등급을 변경시킴 관리자가 이 유저의 등급을 수정할 때 사용,
        if (userRole.equals(UserRole.BRONZE)) userRole = UserRole.SILVER;
        else if (userRole.equals(UserRole.SILVER)) userRole = UserRole.GOLD;
        else if (userRole.equals(UserRole.GOLD)) userRole = UserRole.BLACKLIST;
        else if (userRole.equals(UserRole.BLACKLIST)) userRole = UserRole.BRONZE;
    }
}
