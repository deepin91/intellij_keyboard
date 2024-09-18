package service;


import dto.BoardCommentsDto;
import dto.BoardDto;
import entity.BoardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.BoardRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {
    @Autowired
    private BoardRepository boardRepository;

    // 날짜 형식 지정 (LocalDateTime -> String 변환용)
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    // 게시글 저장 (DTO -> Entity 변환 후 데이터베이스에 저장)
    @Override
    public int insertBoard(BoardDto boardDto) {
        BoardEntity boardEntity = convertDtoToEntity(boardDto);
        boardEntity.setBCreatedDt(LocalDateTime.now());  // 생성 시간 설정
        boardRepository.save(boardEntity);
        return boardEntity.getBIdx();
    }
//        BoardEntity boardEntity = new BoardEntity();
//        boardEntity.setBTitle(boardDto.getBTitle());
//        boardEntity.setBContents(boardDto.getBContents());
//        boardEntity.setUserNickname(boardDto.getUserNickname());
//        boardEntity.setBViews(boardDto.getBViews());
//        boardEntity.setBImg(boardDto.getBImg());
//        boardEntity.setBCreatedDt(LocalDateTime.now());
//
//        boardRepository.save(boardEntity);
//
//        return boardEntity.getBIdx();


    // 게시글 상세 조회 (Entity -> DTO 변환 후 반환)
    @Override
    public BoardDto boardDetail(int bIdx) {
        BoardEntity boardEntity = boardRepository.findById(bIdx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        return convertEntityToDto(boardEntity);
//        BoardEntity boardEntity = boardRepository.findById(bIdx)
//                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
//
//        BoardDto boardDto = new BoardDto();
//        boardDto.setBIdx(boardEntity.getBIdx());
//        boardDto.setBTitle(boardEntity.getBTitle());
//        boardDto.setBContents(boardEntity.getBContents());
//        boardDto.setUserNickname(boardEntity.getUserNickname());
//        boardDto.setBViews(boardEntity.getBViews());
//        boardDto.setBImg(boardEntity.getBImg());
//        boardDto.setBCreatedDt(boardEntity.getBCreatedDt().toString());  // LocalDateTime -> String 변환
//
//        return boardDto;
    }

    @Override
    public List<BoardCommentsDto> boardCommentsList(int bIdx) throws Exception {
        return null;
    }


    // 게시글 조회수 증가
    @Override
    public int updateViews(int bIdx) {
        BoardEntity boardEntity = boardRepository.findById(bIdx)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
        boardEntity.setBViews(boardEntity.getBViews() + 1);  // 조회수 증가
        boardRepository.save(boardEntity);
        return bIdx;
    }

    @Override
    public int insertComments(BoardCommentsDto boardCommentsDto) throws Exception {
        return 0;
    }
//        BoardEntity boardEntity = boardRepository.findById(bIdx)
//                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));
//
//        //조회수 증가
//        boardEntity.setBViews(boardEntity.getBViews() + 1);
//        boardRepository.save(boardEntity);


    //댓글 입력은 아직 구현 x
//        @Override
//        public void insertComments (BoardCommentsDto boardCommentsDto){
//
//        }

    // 댓글 리스트도 아직 구현 x
//        @Override
//        public List<BoardCommentsDto> boardCommentsList ( int bIdx){
//            return null;
//        }

    // 게시글 목록 조회 (Entity -> DTO 변환 후 반환)
    @Override
    public List<BoardDto> boardList() {
        List<BoardEntity> boardEntities = boardRepository.findAll();  // 중복 선언 제거
        return boardEntities.stream()
                .map(this::convertEntityToDto)
                .collect(Collectors.toList());
    }
    // Entity 목록을 DTO 목록으로 변환
//        return boardEntites.stream().map(boardEntity -> {
//            BoardDto boardDto = new BoardDto();
//            boardDto.setBIdx(boardEntity.getBIdx());
//            boardDto.setBTitle(boardEntity.getBTitle());
//            boardDto.setBContents(boardEntity.getBContents());
//            boardDto.setUserNickname(boardEntity.getUserNickname());
//            boardDto.setBViews(boardEntity.getBViews());
//            boardDto.setBImg(boardEntity.getBImg());
//            boardDto.setBCreatedDt(boardEntity.getBCreatedDt().toString());  // LocalDateTime -> String 변환
//            return boardDto;
//        }).collect(Collectors.toList());


    //게시글 수정(DTO -> Entity 변환 후 데이터베이스에 저장)
    @Override
    public int updateBoard(BoardDto boardDto) {
        BoardEntity boardEntity = boardRepository.findById(boardDto.getBIdx())
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        // DTO 데이터를 Entity에 반영
        boardEntity.setBTitle(boardDto.getBTitle());
        boardEntity.setBContents(boardDto.getBContents());
        boardEntity.setUserNickname(boardDto.getUserNickname());
        boardEntity.setBImg(boardDto.getBImg());
        boardRepository.save(boardEntity);  // 수정된 게시글 저장
        return 0;
    }

    //    // DTO -> Entity 변환
    private BoardEntity convertDtoToEntity(BoardDto boardDto) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBTitle(boardDto.getBTitle());
        boardEntity.setBContents(boardDto.getBContents());
        boardEntity.setUserNickname(boardDto.getUserNickname());
        boardEntity.setBViews(boardDto.getBViews());
        boardEntity.setBImg(boardDto.getBImg());
        return boardEntity;
    }

    private BoardDto convertEntityToDto(BoardEntity boardEntity) {
        BoardDto boardDto = new BoardDto();
        boardDto.setBIdx(boardEntity.getBIdx());
        boardDto.setBTitle(boardEntity.getBTitle());
        boardDto.setBContents(boardEntity.getBContents());
        boardDto.setUserNickname(boardEntity.getUserNickname());
        boardDto.setBViews(boardEntity.getBViews());
        boardDto.setBImg(boardEntity.getBImg());

        // bCreatedDt가 null인지 확인 후 처리
        if (boardEntity.getBCreatedDt() != null) {
            boardDto.setBCreatedDt(boardEntity.getBCreatedDt().format(formatter));  // LocalDateTime -> String 변환
        } else {
            boardDto.setBCreatedDt(null);  // null인 경우 처리
        }

        return boardDto;
    }
}

