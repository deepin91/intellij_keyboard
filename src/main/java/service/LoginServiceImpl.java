package service;

import dto.LoginDto;
import dto.UserDto;
import mapper.LoginMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class LoginServiceImpl implements  LoginService{

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDto login(LoginDto loginDto) throws Exception {
        return loginMapper.login(loginDto);
    }

    @Override
    public int registerUser(UserDto userDto) throws Exception {
        return 0;
    }

    @Override
    public int registUser(UserDto userDto) throws Exception {
        // 패스워드를 암호화해서 새로 저장
        userDto.setUserPassword(passwordEncoder.encode(userDto.getUserPassword()));
        return loginMapper.registerUser(userDto);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDto = loginMapper.selectUserByUserId(username);
        if (userDto == null) {
            throw new UsernameNotFoundException(username);
        }

        // String username, String password, boolean enabled, boolean accountNonExpired,
        // boolean credentialsNonExpired, boolean accountNonLocked,
        // Collection<? extends GrantedAuthority> authorities
        return new User(userDto.getUserId(), userDto.getUserPassword(),
                true, true, true, true, new ArrayList<>());
    }
}
