package service;

import dto.BoardCommentsDto;
import dto.NoticeDto;
import dto.SwitchSoundDto;

import java.util.List;

public interface KeyboardService {
    int insertSound(SwitchSoundDto switchSoundDto)throws Exception;

    int insertBoardComments(BoardCommentsDto boardCommentsDto)throws Exception;

    List<BoardCommentsDto> selectBoardCommentsList(int bIdx)throws Exception;

    int updateBoardComments(BoardCommentsDto boardCommentsDto)throws Exception;

    int deleteBoardComments(int bcIdx)throws Exception;

    List<NoticeDto> noticeList()throws Exception;

    NoticeDto noticeDetail(int nIdx)throws Exception;
}
