package wad.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.domain.Account;
import wad.domain.Word;
import wad.domain.view.WordView;
import wad.repository.AccountRepository;
import wad.repository.WordRepository;

@Service
public class WordService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    WordRepository wordRepository;
    
    public Word findOne(Long id)
    {
        return wordRepository.findOne(id);
    }
    
    public Word saveWord(Long id, Word word)
    {
        Word w_found = wordRepository.findOne(id);
        w_found.setContent(word.getContent());
        w_found.setLangCode(word.getLangCode());
        w_found.setPronounce(word.getPronounce());
        w_found.setType(word.getType());
        
        return wordRepository.save(w_found);
    }
    
    public void deleteWord(Long id)
    {
        wordRepository.delete(id);
    }
    
    public List<Word> findWordsByUsername(String username) {
        Account account = accountRepository.findByUsername(username);
        List<Word> words_list = account.getWords();
        List<Word> word_type_list = new ArrayList();
        
        words_list.sort(Comparator.comparing(Word::getId));
        
        for (Word word : words_list) {
            if ("word".equals(word.getType())) {
                word_type_list.add(word);
            }
        }

        return word_type_list;
    }
    
    public List<Word> findWordsByUsernameLimitedBy(String username, Long limit) {
        Account account = accountRepository.findByUsername(username);
        List<Word> words_list = account.getWords();
        List<Word> word_type_list = new ArrayList();
        
        words_list.sort(Comparator.comparing(Word::getId));
        
        for (Word word : words_list) {
            if (word_type_list.size()<limit) {
                if ("word".equals(word.getType())) {
                    word_type_list.add(word);
                }
            }
            else
            {
                break;
            }
        }
        
        System.out.println("word_type_list = "+word_type_list.size());
        return word_type_list;
    }
    
    public List<Word> findWordsContainSubString(String subString, String username)
    {
       List<Word> words_list=this.findWordsByUsername(username);
       List<Word> words_list_containSubString=new ArrayList();
       
        if (words_list==null) {
            return new ArrayList();
        }
        for (Word word : words_list) {
            if (word.getContent().toLowerCase().contains(subString.toLowerCase())) {
                words_list_containSubString.add(word);
            }
        }
        
        return words_list_containSubString;
    }
    
    public WordView findWordViewByUsername(String username, Long wordId) {
        Account account = accountRepository.findByUsername(username);
        Word word = wordRepository.findOne(wordId);
        WordView wordView = new WordView();
        
        if (word==null) {
            return null;
        }
        
        if ((word.getType()==null)|(!"word".equals(word.getType()))) {
            return null;
        }
        
        if (word.getAccount().equals(account)) {
            // Word found.
            wordView.setWord(word);
            wordView.setDelete_word_url("api/words/"+word.getId());
            wordView.setEdit_word_url("words/edit/"+word.getId());
            
            List<Word> words = this.findWordsByUsername(account.getUsername());
            Integer word_index = words.indexOf(word);
            
            if (word_index>0) {
                Word prev_word = words.get(word_index-1);
                wordView.setPrevious_word_url((!Objects.equals(prev_word.getId(), word.getId()))?("/words/"+prev_word.getId().toString()):(""));
            }
            
            if (word_index+1<words.size()) {
                Word next_word = words.get(word_index+1);
                wordView.setNext_word_url((!Objects.equals(next_word.getId(), word.getId()))?("/words/"+words.get(word_index+1).getId().toString()):(""));
            }
            
            return wordView;
        }

        return null;
    }

    public Word saveWordByUsername(String username, Word word) {
        Date currentDate = new Date();
        Account account = accountRepository.findByUsername(username);
        word.setAccount(account);
        word.setCreatedDate(currentDate);

        word = wordRepository.save(word);

        return word;
    }
}
