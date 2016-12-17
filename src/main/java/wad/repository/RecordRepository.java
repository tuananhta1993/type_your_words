package wad.repository;

import java.util.List;
import org.junit.runner.manipulation.Sortable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import wad.domain.Account;
import wad.domain.Record;

public interface RecordRepository extends JpaRepository<Record, Long> {
    public List<Record> findByisPublic(@Param("isPublic") Boolean isPublic, Pageable pageable);
}