/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wad.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import wad.domain.Account;
import wad.repository.AccountRepository;

@Controller
public class HomeController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", auth.getName());
        Boolean isAuthenticated;
        Long userID;
        isAuthenticated = (!("anonymousUser".equals(auth.getName())));
        model.addAttribute("isAuthenticated", isAuthenticated);
        if (isAuthenticated) {
            userID = accountRepository.findByUsername(auth.getName()).getId();
            model.addAttribute("userID", userID);
        }

        System.out.println();
        return "index";
    }
}
