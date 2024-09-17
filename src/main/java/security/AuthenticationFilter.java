package security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.LoginDto;
import dto.UserDto;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mapper.LoginMapper;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private LoginMapper loginMapper;	// 회원 정조 조회 시 사용
    private Environment env;		// 토큰 생성 시 사용

    public AuthenticationFilter(LoginMapper loginMapper, Environment env) {
        this.loginMapper = loginMapper;
        this.env = env;
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            LoginDto creds = new ObjectMapper().readValue(request.getInputStream(), LoginDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getUserId(),
                            creds.getUserPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // username = userId
        String userName = ((User)authResult.getPrincipal()).getUsername();
        UserDto userDto = loginMapper.selectUserByUserId(userName);
        log.debug(userDto.toString());

        Instant now = Instant.now();
        Long expirationTime = Long.parseLong(env.getProperty("token.expiration-time"));
        String jwtToken = Jwts.builder()
                .claim("name", userDto.getUserName())
                .claim("email", userDto.getUserEmail())
                .setSubject(userDto.getUserId())
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expirationTime, ChronoUnit.MILLIS)))
                .compact();
        log.debug(jwtToken);
    }

}
