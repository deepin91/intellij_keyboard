package service;

import dto.BoardDto;
import dto.NoticeDto;

import java.util.List;

public interface JpaService {
    List<NoticeDto> noticeList();

    void insertNotice(NoticeDto noticeDto);

    NoticeDto noticeDetail(int nIdx);

    int updateNotice(NoticeDto noticeDto);

    int deleteNotice(int nIdx);

    BoardDto boardDetail(int bIdx);
}
