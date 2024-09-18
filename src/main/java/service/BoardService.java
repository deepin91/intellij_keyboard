package service;


import dto.BoardCommentsDto;
import dto.BoardDto;

import java.util.List;

public interface BoardService {
    int insertBoard(BoardDto boardDto)throws Exception;
    BoardDto boardDetail(int bIdx)throws Exception;
    List<BoardCommentsDto> boardCommentsList(int bIdx)throws Exception;
    int updateViews(int bIdx)throws Exception;
    int insertComments(BoardCommentsDto boardCommentsDto)throws Exception;
    List<BoardDto> boardList()throws Exception;
    int updateBoard(BoardDto boardDto)throws Exception;
}
