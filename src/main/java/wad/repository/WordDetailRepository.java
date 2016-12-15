package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.WordDetail;

public interface WordDetailRepository extends JpaRepository<WordDetail, Long> {
}