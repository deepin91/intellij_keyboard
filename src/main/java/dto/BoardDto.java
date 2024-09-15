package dto;

import lombok.Data;

@Data
public class BoardDto {
    private int bIdx;
    private String bTitle;
    private String bContents;
    private String userNickname;
    private int bViews;
    private String bCreatedDt;
    private String bImg;
}
