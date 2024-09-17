package service;

import dto.LoginDto;
import dto.UserDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface LoginService {
    public UserDto login(LoginDto loginDto) throws Exception;
    public int registUser(UserDto userDto) throws Exception;

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
