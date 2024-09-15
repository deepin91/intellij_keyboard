package controller;


import dto.BoardCommentsDto;
import dto.BoardDto;
import dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        UserDto userDto = (UserDto) authentication.getPrincipal();
        boardDto.setUserNickname(userDto.getUserId());
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

    @GetMapping("/api/board/detail/{b_idx}/{update}")
    public ResponseEntity<Map<String,Object>> boardDetail(@PathVariable("b_idx") int bIdx, @PathVariable("update") int update) throws Exception{
        BoardDto boardDto = boardService.boardDetail(bIdx);
        List<BoardCommentsDto> boardCommentsDto = boardService.boardCommentsList(bIdx); //이 코멘트는 작성된걸 불러오는 것 아래 comments 는 입력창
        Map<String,Object> map = new HashMap<>();
        if(update == 1) {
            boardService.updateViews(bIdx);
        }
        map.put("boardDetail",boardDto);
        map.put("boardCommentsList",boardCommentsDto);
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }


    @PostMapping("/api/comments")
    public ResponseEntity<Object> insertComments(@RequestBody BoardCommentsDto boardCommentsDto, Authentication authentication){
        UserDto userDto = (UserDto) authentication.getPrincipal();
        boardCommentsDto.setUserNickname(userDto.getUserId()); //안 나오면 이건  userId가 아니라 user_nickname  or userNickname 으로 바꿔야
        boardService.insertComments(boardCommentsDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping("/api/boardList")
    public ResponseEntity<List<BoardDto>> boardList() throws Exception{
        List<BoardDto> boardDto = boardService.boardList();
        return ResponseEntity.status(HttpStatus.OK).body(boardDto);
    }
    @GetMapping("/api/comments/{b_idx}")
    public ResponseEntity<List<BoardCommentsDto>> getComments(@PathVariable("b_idx") int bIdx){ // PathVariable 뒤에 bIdx 아니고 b_Idx일 가능성 높음 ->고침
        List<BoardCommentsDto> boardCommentsDto = boardService.boardCommentsList(bIdx);
        return ResponseEntity.status(HttpStatus.OK).body(boardCommentsDto);
    }

    @PutMapping("/api/update/board")
    public ResponseEntity<Object> updateBoard(@RequestBody BoardDto boardDto){
        boardService.updateBoard(boardDto);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
