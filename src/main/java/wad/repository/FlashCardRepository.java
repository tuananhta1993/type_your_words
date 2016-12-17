package wad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.FlashCard;

public interface FlashCardRepository extends JpaRepository<FlashCard, Long> {
}
