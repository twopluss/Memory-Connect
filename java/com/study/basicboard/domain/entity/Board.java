package com.study.basicboard.domain.entity;

import com.study.basicboard.domain.BaseEntity;
import com.study.basicboard.domain.dto.BoardDto;
import com.study.basicboard.domain.enum_class.BoardCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class Board extends BaseEntity { // baseEntity를 상속받은 이유는 공통 필드 및 로직의 재사용 때문이다

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;   // 제목
    private String years;   // 년도
    private String body;    // 본문

    @Enumerated(EnumType.STRING)
    private BoardCategory category; // 카테고리

    @ManyToOne(fetch = FetchType.LAZY)  // 이해가 안가 -------------------------
    private User user;  //현재 엔티티가 User 엔티티와 Many-to-One 관계를 가지며

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Like> likes;       // 좋아요
    private Integer likeCnt;        // 좋아요 수

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    private List<Comment> comments; // 댓글
    private Integer commentCnt;     // 댓글 수

    //좋아요 수, 댓글 수를 저장한 이유는 리스트에서 정렬할 때 더 빠른 조회를 위해 따로 저장

    @OneToOne(fetch = FetchType.LAZY)
    private UploadImage uploadImage;

    public void update(BoardDto dto) {
        this.title = dto.getTitle();
        this.body = dto.getBody();
        this.years = dto.getYears();
    }

    public void likeChange(Integer likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void commentChange(Integer commentCnt) {
        this.commentCnt = commentCnt;
    }

    public void setUploadImage(UploadImage uploadImage) {
        this.uploadImage = uploadImage;
    }

}
