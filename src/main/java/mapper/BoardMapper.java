package mapper;

import dto.BoardCommentsDto;
import dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {
    int insertBoard(BoardDto boardDto);
    BoardDto boardDetail(int bIdx);
    List<BoardCommentsDto> boardCommentsList(int bIdx);
    void updateViews(int bIdx);
    void insertComments(BoardCommentsDto boardCommentsDto);
    List<BoardDto> boardList();
    void updateBoard(BoardDto boardDto);
}
