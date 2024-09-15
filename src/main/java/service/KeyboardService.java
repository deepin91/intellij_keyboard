package service;

import dto.BoardCommentsDto;
import dto.NoticeDto;
import dto.SwitchSoundDto;

import java.util.List;

public interface KeyboardService {
    void insertSound(SwitchSoundDto switchSoundDto);

    int insertBoardComments(BoardCommentsDto boardCommentsDto);

    List<BoardCommentsDto> selectBoardCommentsList(int bIdx);

    void updateBoardComments(BoardCommentsDto boardCommentsDto);

    void deleteBoardComments(int bcIdx);

    List<NoticeDto> noticeList();

    NoticeDto noticeDetail(int nIdx);
}
