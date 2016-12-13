//package wad.domain;
//
//import java.util.List;
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.ManyToMany;
//import org.springframework.data.jpa.domain.AbstractPersistable;
//
//@Entity
//public class Account extends AbstractPersistable<Long> {
//
//    private String username;
//    private String password;
//    @ManyToMany(fetch = FetchType.EAGER)
//    private List<Permission> permissions;
//
//    public List<Permission> getPermissions() {
//        return permissions;
//    }
//
//    public void setPermissions(List<Permission> permissions) {
//        this.permissions = permissions;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//
//}
