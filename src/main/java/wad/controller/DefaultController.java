package wad.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import wad.domain.Account;
import wad.domain.Permission;
import wad.repository.AccountRepository;
import wad.repository.PermissionRepository;
import wad.service.AuthenticationService;

@Controller
public class DefaultController {
    @Autowired
    private AuthenticationService authService;
    
    @PostConstruct
    public void init() {
//         Temporary disable this method. Anh. 15122016
        authService.initDefaultUsers();
    }

//    @RequestMapping("*")
//    public String handleDefault() {
//        return "redirect:/home";
//    }
}
