package wad.auth;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wad.domain.Account;
import wad.domain.Permission;
import wad.repository.AccountRepository;
import wad.repository.PermissionRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionRepository permissionRepository;
    
    @Autowired
    private AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        Permission pUSER = new Permission();
        pUSER.setName("USER");
        permissionRepository.save(pUSER);
        
        Permission pADMIN = new Permission();
        pADMIN.setName("ADMIN");
        permissionRepository.save(pADMIN);
        
        Account user1 = new Account();
        user1.setUsername("larry");
        user1.setPassword(passwordEncoder.encode("larry"));
        List<Permission> permissions1 = new ArrayList<>();
        permissions1.add(pUSER);
        user1.setPermissions(permissions1);

        accountRepository.save(user1);
        
        Account user2 = new Account();
        user2.setUsername("moe");
        user2.setPassword(passwordEncoder.encode("moe"));
        List<Permission> permissions2 = new ArrayList<>();
        permissions2.add(pUSER);
        permissions2.add(pADMIN);
        user2.setPermissions(permissions2);

        accountRepository.save(user2);
        
        Account user3 = new Account();
        user3.setUsername("curly");
        user3.setPassword(passwordEncoder.encode("curly"));
        List<Permission> permissions3 = new ArrayList<>();
        permissions3.add(pADMIN);
        user3.setPermissions(permissions3);

        accountRepository.save(user3);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        if (account == null) {
            throw new UsernameNotFoundException("No such user: " + username);
        }
        
        List<Permission> permissions = account.getPermissions();
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        
        for (Permission permission : permissions) {
            grantedAuthorities.add(new SimpleGrantedAuthority(permission.getName()));
        }
        
        return new org.springframework.security.core.userdetails.User(
        account.getUsername(),
        account.getPassword(),
        true,
        true,
        true,
        true,
        grantedAuthorities);
        
    }
}
