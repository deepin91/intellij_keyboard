package dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private int userProfileIdx;
    private String userId;
    private String userNickname;
    private String userInfo;
    private String profileImg;
    private int bIdx;
}
