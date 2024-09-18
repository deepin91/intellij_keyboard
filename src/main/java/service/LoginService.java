package service;


import dto.LoginDto;
import dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginService {
    // 로그인 메서드: 로그인 성공 시 UserDto 반환, 실패 시 null 반환 또는 커스텀 예외 처리
    UserDto login(LoginDto loginDto) throws Exception;

    // 사용자 등록 메서드: 성공 시 1 반환, 실패 시 0 또는 예외 처리
    int registerUser(UserDto userDto) throws Exception;


    int registUser(UserDto userDto) throws Exception;

    // Spring Security 사용자 인증 메서드
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
