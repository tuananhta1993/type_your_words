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
//    @Autowired
//    private AuthenticationService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    AccountRepository accountRepository;
    
    @PostConstruct
    public void init() {
        Permission pUSER = new Permission();
        if (permissionRepository.findByName("USER") == null) {
            pUSER.setName("USER");
            permissionRepository.save(pUSER);
        } else {
            pUSER = permissionRepository.findByName("USER");
        }

        Permission pADMIN = new Permission();
        if (permissionRepository.findByName("ADMIN") == null) {
            pADMIN.setName("ADMIN");
            permissionRepository.save(pADMIN);
        } else {
            pADMIN = permissionRepository.findByName("admin");
        }

        if (accountRepository.findByUsername("admin") == null) {
            Account user1 = new Account();
            user1.setUsername("admin");
            user1.setPassword(passwordEncoder.encode("admin"));
            user1.setEmail("admin@tatuananh.com");

            List<Permission> permissions1 = new ArrayList<>();
            permissions1.add(pUSER);
            user1.setPermissions(permissions1);

            accountRepository.save(user1);
        }

        if (accountRepository.findByUsername("useradmin") == null) {
            Account user2 = new Account();
            user2.setUsername("useradmin");
            user2.setPassword(passwordEncoder.encode("useradmin"));
            user2.setEmail("mail@tatuananh.com");
            List<Permission> permissions2 = new ArrayList<>();
            permissions2.add(pUSER);
            permissions2.add(pADMIN);
            user2.setPermissions(permissions2);

            accountRepository.save(user2);
        }

        if (accountRepository.findByUsername("user") == null) {
            Account user3 = new Account();
            user3.setUsername("user");
            user3.setPassword(passwordEncoder.encode("user"));
            user3.setEmail("mb@tatuananh.com");

            List<Permission> permissions3 = new ArrayList<>();
            permissions3.add(pADMIN);
            user3.setPermissions(permissions3);

            accountRepository.save(user3);
        }
    }

//    @RequestMapping("*")
//    public String handleDefault() {
//        return "redirect:/home";
//    }
}
