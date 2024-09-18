package service;

import dto.BoardDto;
import dto.NoticeDto;

import java.util.List;

public interface JpaService {
    List<NoticeDto> noticeList()throws Exception;

    int insertNotice(NoticeDto noticeDto)throws Exception;

    NoticeDto noticeDetail(int nIdx)throws Exception;

    int updateNotice(NoticeDto noticeDto)throws Exception;

    int deleteNotice(int nIdx)throws Exception;

    BoardDto boardDetail(int bIdx)throws Exception;
}
