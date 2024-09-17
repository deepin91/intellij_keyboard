package service;


import dto.BoardCommentsDto;
import dto.BoardDto;
import entity.BoardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BoardRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService{
    @Autowired
    private BoardRepository boardRepository;

    // 게시글 저장 (DTO -> Entity 변환 후 데이터베이스에 저장)
    @Override
    public int insertBoard(BoardDto boardDto) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBTitle(boardDto.getBTitle());
        boardEntity.setBContents(boardDto.getBContents());
        boardEntity.setUserNickname(boardDto.getUserNickname());
        boardEntity.setBViews(boardDto.getBViews());
        boardEntity.setBImg(boardDto.getBImg());
        boardEntity.setBCreatedDt(LocalDateTime.now());

        boardRepository.save(boardEntity);

        return boardEntity.getBIdx();
    }

    // 게시글 상세 조회 (Entity -> DTO 변환 후 반환)
    @Override
    public BoardDto boardDetail(int bIdx) {
        BoardEntity boardEntity = boardRepository.findById(bIdx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        BoardDto boardDto = new BoardDto();
        boardDto.setBIdx(boardEntity.getBIdx());
        boardDto.setBTitle(boardEntity.getBTitle());
        boardDto.setBContents(boardEntity.getBContents());
        boardDto.setUserNickname(boardEntity.getUserNickname());
        boardDto.setBViews(boardEntity.getBViews());
        boardDto.setBImg(boardEntity.getBImg());
        boardDto.setBCreatedDt(boardEntity.getBCreatedDt().toString());  // LocalDateTime -> String 변환

        return boardDto;
    }


    // 게시글 조회수 증가
    @Override
    public void updateViews(int bIdx) {
        BoardEntity boardEntity = boardRepository.findById(bIdx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        //조회수 증가
        boardEntity.setBViews(boardEntity.getBViews() + 1);
        boardRepository.save(boardEntity);
    }

    //댓글 입력은 아직 구현 x
    @Override
    public void insertComments(BoardCommentsDto boardCommentsDto) {

    }

    // 댓글 리스트도 아직 구현 x
    @Override
    public List<BoardCommentsDto> boardCommentsList(int bIdx) {
        return null;
    }

    // 게시글 목록 조회 (Entity -> DTO 변환 후 반환)
    @Override
    public List<BoardDto> boardList() {
        List<BoardEntity> boardEntites = boardRepository.findAll();

        // Entity 목록을 DTO 목록으로 변환
        return boardEntites.stream().map(boardEntity -> {
            BoardDto boardDto = new BoardDto();
            boardDto.setBIdx(boardEntity.getBIdx());
            boardDto.setBTitle(boardEntity.getBTitle());
            boardDto.setBContents(boardEntity.getBContents());
            boardDto.setUserNickname(boardEntity.getUserNickname());
            boardDto.setBViews(boardEntity.getBViews());
            boardDto.setBImg(boardEntity.getBImg());
            boardDto.setBCreatedDt(boardEntity.getBCreatedDt().toString());  // LocalDateTime -> String 변환
            return boardDto;
        }).collect(Collectors.toList());
    }

    //게시글 수정(DTO -> Entity 변환 후 데이터베이스에 저장)
    @Override
    public void updateBoard(BoardDto boardDto) {
        BoardEntity boardEntity = boardRepository.findById(boardDto.getBIdx())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // DTO 데이터를 Entity에 반영
        boardEntity.setBTitle(boardDto.getBTitle());
        boardEntity.setBContents(boardDto.getBContents());
        boardEntity.setUserNickname(boardDto.getUserNickname());
        boardEntity.setBImg(boardDto.getBImg());
        boardRepository.save(boardEntity);  // 수정된 게시글 저장
    }

//    // DTO -> Entity 변환
//    private Board convertDtoToEntity(BoardDto boardDto) {
//        Board board = new Board();
//        board.setTitle(boardDto.getBTitle());
//        board.setContents(boardDto.getBContents());
//        board.setUserNickname(boardDto.getUserNickname());
//        board.setViews(boardDto.getBViews());
//        board.setImgUrl(boardDto.getBImg());
//        board.setCreatedAt(LocalDateTime.now());
//        return board;
//    }
//
//    // Entity -> DTO 변환
//    private BoardDto convertEntityToDto(Board board) {
//        BoardDto boardDto = new BoardDto();
//        boardDto.setBIdx(board.getId().intValue());
//        boardDto.setBTitle(board.getTitle());
//        boardDto.setBContents(board.getContents());
//        boardDto.setUserNickname(board.getUserNickname());
//        boardDto.setBViews(board.getViews());
//        boardDto.setBImg(board.getImgUrl());
//        boardDto.setBCreatedDt(board.getCreatedAt().toString());
//        return boardDto;
//    }
}
