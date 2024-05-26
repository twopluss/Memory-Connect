package com.study.basicboard.service;

import com.study.basicboard.domain.dto.UserCntDto;
import com.study.basicboard.domain.dto.UserDto;
import com.study.basicboard.domain.dto.UserJoinRequest;
import com.study.basicboard.domain.entity.Comment;
import com.study.basicboard.domain.entity.Like;
import com.study.basicboard.domain.entity.User;
import com.study.basicboard.domain.enum_class.UserRole;
import com.study.basicboard.repository.CommentRepository;
import com.study.basicboard.repository.LikeRepository;
import com.study.basicboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final BCryptPasswordEncoder encoder;


    //joinValid(), editValid()는 회원 가입, 정보 수정 시 비어있는지, 글자수가 초과하는지,
    // 중복되는지 등을 상황과 변수에 맞게 판단 후 하나라도 조건을 만족하지 못하면 BindingResult를 return 함으로써 회원가입, 정보 수정을 못하게 함
    public BindingResult joinValid(UserJoinRequest req, BindingResult bindingResult)
    {
        if (req.getLoginId().isEmpty()) {
            bindingResult.addError(new FieldError("req", "loginId", "아이디가 비어있습니다."));
        } else if (req.getLoginId().length() > 10) {
            bindingResult.addError(new FieldError("req", "loginId", "아이디가 10자가 넘습니다."));
        } else if (userRepository.existsByLoginId(req.getLoginId())) {
            bindingResult.addError(new FieldError("req", "loginId", "아이디가 중복됩니다."));
        }

        if (req.getPassword().isEmpty()) {
            bindingResult.addError(new FieldError("req", "password", "비밀번호가 비어있습니다."));
        }

        if (!req.getPassword().equals(req.getPasswordCheck())) {
            bindingResult.addError(new FieldError("req", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (req.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("req", "nickname", "닉네임이 비어있습니다."));
        } else if (req.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("req", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (userRepository.existsByNickname(req.getNickname())) {
            bindingResult.addError(new FieldError("req", "nickname", "닉네임이 중복됩니다."));
        }

        return bindingResult;
    }

    public void join(UserJoinRequest req) {
        userRepository.save(req.toEntity( encoder.encode(req.getPassword()) ));
    }
    // save는  Spring Data JPA에서 제공하는 기본 메소드 중 하나
    // toEntity() 메소드는 UserJoinRequest 객체를 받아와서 User 엔티티로 변환하는 역할을 수행합니다. 이때, 비밀번호는 encoder.encode(req.getPassword())를 통해 암호화됩니다.
    public User myInfo(String loginId) {
        return userRepository.findByLoginId(loginId).get();
    }
    //단순히 주어진 로그인 아이디를 가진 사용자의 정보를 조회 , <사용자 정보를 반환>하기 때문에

    public BindingResult editValid(UserDto dto, BindingResult bindingResult, String loginId) // 정보수정
    {
        User loginUser = userRepository.findByLoginId(loginId).get(); // 사옹자의 아이디를 가져오고 그 엔티티 자체의 정보를 가져오라는 말

        if (dto.getNowPassword().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 비어있습니다."));
        } else if (!encoder.matches(dto.getNowPassword(), loginUser.getPassword())) {
            bindingResult.addError(new FieldError("dto", "nowPassword", "현재 비밀번호가 틀렸습니다."));
        }

        if (!dto.getNewPassword().equals(dto.getNewPasswordCheck())) {
            bindingResult.addError(new FieldError("dto", "newPasswordCheck", "비밀번호가 일치하지 않습니다."));
        }

        if (dto.getNickname().isEmpty()) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 비어있습니다."));
        } else if (dto.getNickname().length() > 10) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 10자가 넘습니다."));
        } else if (!dto.getNickname().equals(loginUser.getNickname()) && userRepository.existsByNickname(dto.getNickname())) {
            bindingResult.addError(new FieldError("dto", "nickname", "닉네임이 중복됩니다."));
        }

        return bindingResult;
    }

    @Transactional // 두개의 작업이 성곻해야 동작할수 있게 트렌젝션으로 묶어버림
    public void edit(UserDto dto, String loginId) {
        User loginUser = userRepository.findByLoginId(loginId).get();

        if (dto.getNewPassword().equals("")) {
            loginUser.edit(loginUser.getPassword(), dto.getNickname());
        } else {
            loginUser.edit(encoder.encode(dto.getNewPassword()), dto.getNickname());
        }
    }

    @Transactional // 내부적으로 문제를 일으킬수 있기 때문에 모두가 성공해야 성공하는 트랜젝션으로 묶어준것이다
    public Boolean delete(String loginId, String nowPassword) {
        User loginUser = userRepository.findByLoginId(loginId).get();

        if (encoder.matches(nowPassword, loginUser.getPassword())) {
            //encoder.matches() 메소드를 사용하여 입력한 현재 비밀번호를 실제 사용자의 비밀번호와 비교합니다.
            List<Like> likes = likeRepository.findAllByUserLoginId(loginId); // 좋아요가 들어간 게시글을 가져옴
            for (Like like : likes) {
                like.getBoard().likeChange( like.getBoard().getLikeCnt() - 1 ); // 사용자가 좋아요를 누른 게시글들을 가져와 좋아요 하나씩 줄임
                //like.getBoard를 통해서 .getLikeCnt를 불러올수 있는 이유는 getBoard에서 Board가 반환 타입이 Board 이기 때문
            }

            List<Comment> comments = commentRepository.findAllByUserLoginId(loginId);
            for (Comment comment : comments) {
                comment.getBoard().commentChange( comment.getBoard().getCommentCnt() - 1 );
            }

            userRepository.delete(loginUser);
            return true;
        } else {
            return false;
        }
    }

    public Page<User> findAllByNickname(String keyword, PageRequest pageRequest) {
        return userRepository.findAllByNicknameContains(keyword, pageRequest);
    }

    @Transactional
    public void changeRole(Long userId) {
        User user = userRepository.findById(userId).get();
        user.changeRole();
    }

    //사용자의 역할 별 수를 조회하여 이를 UserCntDto 객체에 담아 반환하는 메소드입니다.
    public UserCntDto getUserCnt() {
        return UserCntDto.builder()
                .totalUserCnt(userRepository.count())
                .totalAdminCnt(userRepository.countAllByUserRole(UserRole.ADMIN))
                .totalBronzeCnt(userRepository.countAllByUserRole(UserRole.BRONZE))
                .totalSilverCnt(userRepository.countAllByUserRole(UserRole.SILVER))
                .totalGoldCnt(userRepository.countAllByUserRole(UserRole.GOLD))
                .totalBlacklistCnt(userRepository.countAllByUserRole(UserRole.BLACKLIST))
                .build();
    }
}
