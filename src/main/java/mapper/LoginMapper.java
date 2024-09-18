package mapper;

import dto.LoginDto;
import dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {

    // 로그인 처리
    public UserDto login(LoginDto loginDto) throws Exception;

    // 사용자 등록, 성공 여부 반환
    public int registerUser(UserDto userDto) throws Exception;

    // 사용자 ID로 사용자 정보 조회
    public UserDto selectUserByUserId(String userId);

}
