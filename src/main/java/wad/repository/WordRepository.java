package wad.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import wad.domain.Account;
import wad.domain.Word;

public interface WordRepository extends JpaRepository<Word, Long> {
    public List<Word> findWordsByAccount(Account adminAccount);
}