/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wad.repository.AccountRepository;

@Controller
public class HomeController {
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/")
    public String home(@RequestParam(name="optPrg", required=false) Long optPrg, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        Boolean isAuthenticated;
        Long userID;
        isAuthenticated = (!("anonymousUser".equals(auth.getName())));
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            userID = accountRepository.findByUsername(auth.getName()).getId();
            model.addAttribute("userID", userID);
            
            if ((optPrg!=null)&&(optPrg>0)) {
                // List<Word> private_words=new ArrayList();
                // private_words=wordService.findWordsByUsernameLimitedBy(auth.getName(),optPrg);
                // model.addAttribute("private_words", private_words);
            }
        }

        return "index";
    }
}
