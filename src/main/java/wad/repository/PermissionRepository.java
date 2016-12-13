package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wad.domain.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
