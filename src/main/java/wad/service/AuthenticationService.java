package wad.service;

import java.util.ArrayList;
import java.util.List;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import wad.domain.Account;
import wad.domain.Permission;
import wad.repository.AccountRepository;
import wad.repository.PermissionRepository;

@Service
public class AuthenticationService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    PermissionRepository permissionRepository;

    @Autowired
    AccountRepository accountRepository;

    public void initDefaultUsers() {
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

    public Account registerNewAccount(Account account) {
        Permission pUSER = permissionRepository.findByName("USER");

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        List<Permission> permissions = new ArrayList<>();
        permissions.add(pUSER);
        account.setPermissions(permissions);

        account=accountRepository.save(account);
        System.out.println(account.getUsername());
        return account;

    }

    public void autologin(String username, String password) {
        System.out.println(username);
        
    }
}
