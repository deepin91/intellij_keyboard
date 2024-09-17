package controller;

import dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import service.LoginService;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
public class RestLoginApiController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/api/regist")
    public ResponseEntity<Object> regist(@RequestBody UserDto userDto) throws Exception {
        int registedCount = loginService.registUser(userDto);
        if (registedCount > 0) {
            return ResponseEntity.status(HttpStatus.CREATED).body(registedCount);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(registedCount);
        }
    }

}
