package service;

import dto.BoardCommentsDto;
import dto.BoardDto;

import java.util.List;

public interface BoardService {
    int insertBoard(BoardDto boardDto);
    BoardDto boardDetail(int bIdx);

    List<BoardCommentsDto> boardCommentsList(int bIdx);

    void updateViews(int bIdx);

    void insertComments(BoardCommentsDto boardCommentsDto);

    List<BoardDto> boardList();

    void updateBoard(BoardDto boardDto);
}
