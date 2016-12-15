package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    public Permission findByName(String name);
}