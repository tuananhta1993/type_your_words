/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wad.domain.Account;
import wad.domain.Word;
import wad.repository.AccountRepository;
import wad.repository.WordRepository;

@Service
public class PublicService {
    @Autowired
    WordRepository wordRepository;
    
    @Autowired
    AccountRepository accountRepository;
    
    public List<Word> findDefaultWords()
    {
        String admin="Admin";
        Account adminAccount;
        adminAccount=accountRepository.findByUsername(admin);
        return wordRepository.findWordsByAccount(adminAccount);
    }
}
