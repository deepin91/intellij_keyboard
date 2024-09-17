package mapper;

import dto.LoginDto;
import dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
    public UserDto login(LoginDto loginDto) throws Exception;
    public int registUser(UserDto userDto) throws Exception;
    public UserDto selectUserByUserId(String userId);

}
