package controller;


import dto.NoticeDto;
import dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.JpaService;

import java.util.List;

@Slf4j
@RestController
@Controller
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class NoticeController {
    @Autowired
    JpaService jpaService;

    @GetMapping("/api/notice")
    public ResponseEntity<List<NoticeDto>> noticeList() throws Exception {
        List<NoticeDto> noticeList = jpaService.noticeList();
        if(noticeList != null && noticeList.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(noticeList);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/api/notice/write")
    public ResponseEntity<String> insertNotice(@RequestBody NoticeDto noticeDto) throws Exception {
        try{
            jpaService.insertNotice(noticeDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("등록에 실패했습니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("정상적으로 등록되었습니다.");
    }

    @GetMapping("/api/notice/detail/{nIdx}")
    public ResponseEntity<NoticeDto> noticeDetail(@PathVariable("nIdx") int nIdx) throws Exception{
        NoticeDto noticeDto = jpaService.noticeDetail(nIdx);
        if(noticeDto != null) {
            return ResponseEntity.status(HttpStatus.OK).body(noticeDto);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(null);
        }
    }

    @PutMapping("/api/notice/{nIdx}")
    public ResponseEntity<String> updateNotice(@PathVariable("nIdx") int nIdx,
                                               @RequestBody NoticeDto noticeDto, Authentication authentication) throws Exception {
        try {
            NoticeDto detail = jpaService.noticeDetail(nIdx);
            UserDto userDto = (UserDto) authentication.getPrincipal(); //Principal 부분 확인해봐야함 이름 바꿔야하는지 아닌지
            if (detail.getUserNickname().equals(userDto.getUserNickname()) || userDto.getUserNickname().equals("host01")) { //여기  admin 부분 또한
                noticeDto.setNIdx(nIdx);
                int updatedCount = jpaService.updateNotice(noticeDto);
                if (updatedCount != 1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("수정에 실패했습니다.");
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body("정상적으로 수정되었습니다.");
                }

            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작성자만 수정 가능합니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 해주세요.");
        }
    }

    @DeleteMapping("/api/notice/{nIdx}")
    public ResponseEntity<String> deleteNotice(@PathVariable ("nIdx") int nIdx, Authentication authentication) throws Exception{
        try {
            UserDto userDto = (UserDto) authentication.getPrincipal();
            NoticeDto noticeDto = jpaService.noticeDetail(nIdx);

            log.debug(">>>>>>>>>>>>>>>" +userDto.getUserId());
            if(noticeDto.getUserNickname().equals(noticeDto.getUserNickname()) || noticeDto.getUserNickname().equals("admin")) {
                int deletedCount = jpaService.deleteNotice(nIdx);
                if(deletedCount != 1) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("삭제에 실패했습니다");
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body("정상 삭제되었습니다");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("작성자만 삭제 가능합니다.");
            }
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 해주세요.");
        }
    }
}
