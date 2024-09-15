package dto;

import lombok.Data;

@Data
public class BoardCommentsDto {
    private int bcIdx;
    private String bcComments;
    private String userNickname;
    private int bIdx;
}
