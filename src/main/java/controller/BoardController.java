package controller;


import dto.BoardCommentsDto;
import dto.BoardDto;
import dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import service.BoardService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class BoardController {
    @Autowired
    BoardService boardService;
//    JpaService jpaService;

    @PostMapping("/api/insertBoard")
    public ResponseEntity<Object> insertBoard(@RequestBody BoardDto boardDto, Authentication authentication)
            throws Exception {
        UserDto userDto = (UserDto) authentication.getPrincipal(); // 사용자의 닉네임을 UserDto에서 가져와 BoardDto에 설정
        boardDto.setUserNickname(userDto.getUserNickname());
        int registedCount = boardService.insertBoard(boardDto);
        if (registedCount > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registedCount);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(registedCount);
        }
    }

//    @PostMapping("/api/insertBoard")
//    public ResponseEntity<Object> insertBoard(@RequestBody BoardDto boardDto)
//            throws Exception {
//
//        boardDto.setUserNickname(boardDto.getUserNickname());
//        int registedCount = boardService.insertBoard(boardDto);
//        if (registedCount > 0) {
//            return ResponseEntity.status(HttpStatus.CREATED).body(registedCount);
//        } else {
//            return ResponseEntity.status(HttpStatus.OK).body(registedCount);
//        }
//    }

    // 게시글 상세 조회 + 조회수 증가
    @GetMapping("/api/board/detail/{b_idx}")
    public ResponseEntity<Map<String,Object>> boardDetail(@PathVariable("b_idx") int bIdx, @PathVariable("update") int update) throws Exception{
//        public ResponseEntity<Map<String,Object>> boardDetail(@PathVariable("b_idx") int bIdx) throws Exception{
        BoardDto boardDto = boardService.boardDetail(bIdx);
        List<BoardCommentsDto> boardCommentsDto = boardService.boardCommentsList(bIdx); //이 코멘트는 작성된걸 불러오는 것 아래 comments 는 입력창
        Map<String,Object> map = new HashMap<>();
        if(update == 1) {
            boardService.updateViews(bIdx); // 조회수 업데이트
        }
        map.put("boardDetail",boardDto);
        map.put("boardCommentsList",boardCommentsDto);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    // 댓글 등록
    @PostMapping("/api/comments")
    public ResponseEntity<Object> insertComments(@RequestBody BoardCommentsDto boardCommentsDto, Authentication authentication) throws Exception {
        UserDto userDto = (UserDto) authentication.getPrincipal(); // 사용자 닉네임을 인증 정보에서 가져와 설정
        boardCommentsDto.setUserNickname(userDto.getUserNickname()); //안 나오면 이건  userId가 아니라 user_nickname  or userNickname 으로 바꿔야

        boardService.insertComments(boardCommentsDto); // 댓글 등록에 대한 부분
//        return ResponseEntity.status(HttpStatus.OK).body(null);
        return ResponseEntity.ok().build(); // OK 상태와 빈 본문을 반환
    }


    // 게시글 목록 조회
    @GetMapping("/api/boardList")
    public ResponseEntity<List<BoardDto>> boardList() throws Exception{
        List<BoardDto> boardDto = boardService.boardList();
        return ResponseEntity.status(HttpStatus.OK).body(boardDto);
    }

    // 댓글 목록 조회
    //url에 해당하는 /{b_idx} 부분과 @PathVariable 변수명 이 둘은 똑같아야함
    //url에서 b_idx 썼으면 @PathVariable 뒤에도 꼭 b_idx로 사용! url에서 값을 추출하기 때문에 이름 일치가 중요!
    //자바 변수명(메서드 파라미터 이름, 메서드 내 변수)은 카멜 표기법을 따름.
    //URL 변수와 메서드 파라미터는 일치시켜야 하지만, 자바 코드 내에서는 일관된 카멜 표기법을 사용하는 것이 맞음.
    @GetMapping("/api/comments/{b_idx}")
    public ResponseEntity<List<BoardCommentsDto>> getComments(@PathVariable("b_idx") int bIdx) throws Exception { // PathVariable 뒤에 bIdx 아니고 b_Idx일 가능성 높음 ->고침
        // bIdx가 PathVariable에서 올바르게 매핑되는지 확인 필요
        List<BoardCommentsDto> boardCommentsDto = boardService.boardCommentsList(bIdx);
        return ResponseEntity.status(HttpStatus.OK).body(boardCommentsDto);
    }

    // 게시글 수정
    @PutMapping("/api/update/board")
    public ResponseEntity<Object> updateBoard(@RequestBody BoardDto boardDto) throws Exception {
        boardService.updateBoard(boardDto);
//        return ResponseEntity.status(HttpStatus.OK).body(null);
        return ResponseEntity.ok().build(); // 응답 간소화
    }
}

//b_idx와 bIdx jpa 사용 시 카멜->스네이크 표기법으로 자동 변환해줌.
//즉, DTO의 bIdx 필드는 DB의 b_idx 필드와 자동으로 매핑됨
// API 요청/응답 시(REST API) JSON에서는 카멜표기법을 사용하는 것이 일반적임.