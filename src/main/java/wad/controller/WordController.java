package wad.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import wad.domain.Word;
import wad.domain.WordDetail;
import wad.domain.view.WordView;
import wad.service.WordService;

@Controller
public class WordController {

    @Autowired
    WordService wordService;

    @RequestMapping(value = "/words", method = RequestMethod.GET)
    public String words(@RequestParam(name="s_word", required=false) String s_word, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        Boolean isAuthenticated;
        isAuthenticated = (!("anonymousUser".equals(username)));
        model.addAttribute("isAuthenticated", isAuthenticated);

        List<Word> words=new ArrayList();
        
        if ((s_word!=null)&&(!"".equals(s_word))) {
            words = wordService.findWordsContainSubString(s_word,username);
        }else
        {
            words = wordService.findWordsByUsername(username);
        }
        
        int total_words = 0;
        if (words != null) {
            total_words = words.size();
        }

        model.addAttribute("total_words", total_words);
        model.addAttribute("words", words);

        return "words";
    }
    
    @RequestMapping(value = "/words", method = RequestMethod.POST)
    public String save_words(@Valid
            @ModelAttribute Word word,
            BindingResult bindingResult, HttpServletResponse response) throws IOException {
        if (bindingResult.hasErrors()) {
            return "single_word_form";
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        word = wordService.saveWordByUsername(username, word);

        PrintWriter out = response.getWriter();
        out.println("redirect:/words/" + word.getId().toString());

        return null;
        // return "redirect:/words/"+word.getId().toString();
    }

    @RequestMapping(value = "/words/edit/{id}", method = RequestMethod.GET)
    public String returnEditForm(@PathVariable Long id, Model model) {
        Word word = wordService.findOne(id);
        if (word == null) {
            word = new Word();
        }

        model.addAttribute("word", word);

        return "word_edit_form";
    }

    @RequestMapping(value = "/words/edit/{id}", method = RequestMethod.POST)
    public String updateWord(
            @PathVariable Long id,
            @Valid
            @ModelAttribute Word word,
            BindingResult bindingResult, HttpServletResponse response) throws IOException {
        if (bindingResult.hasErrors()) {
            return "word_edit_form";
        }

        word=wordService.saveWord(id,word);

        PrintWriter out = response.getWriter();
        out.println("redirect:/words/" + word.getId().toString());

        return null;
    }
    
        @RequestMapping(value = "/words/delete/{id}", method = RequestMethod.GET)
    public String returnDeleteForm(@PathVariable Long id, Model model) {
        Word word = wordService.findOne(id);
        if (word == null) {
            word = new Word();
        }

        model.addAttribute("word", word);

        return "word_delete_form";
    }

    @RequestMapping(value = "/words/delete/{id}", method = RequestMethod.POST)
    public String deleteWord( @PathVariable Long id) {
        wordService.deleteWord(id);

        return "redirect:/words";
    }
    
    @RequestMapping(value = "/words/createmultiple", method = RequestMethod.GET)
    public String returnMultipleForm() {
        return "multiple_word_form";
    }
    
    @RequestMapping(value = "/words/createmultiple", method = RequestMethod.POST)
    public String saveMultiple(@RequestParam String words_list, @RequestParam String LangCode, HttpServletResponse response) throws IOException {
        if ("".equals(words_list.trim())) {
            return "multiple_word_form";
        }
        
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        String words[] = words_list.split(",");
        
        for (String word_str : words) {
            if (!"".equals(word_str.trim())) {
                Word word=new Word();
                word.setContent(word_str);
                word.setCreatedDate(new Date());
                word.setType("word");
                word.setLangCode(LangCode);
                word = wordService.saveWordByUsername(username, word);
            }
        }
        
        PrintWriter out = response.getWriter();
        out.println("redirect:/words");

        return null;
    }

    @RequestMapping(value = "/words/createsingle", method = RequestMethod.GET)
    public String createSingle(Model model) {
        model.addAttribute("word", new Word());
        return "single_word_form";
    }

    @RequestMapping(value = "/words/createphrase", method = RequestMethod.GET)
    public String createPhrase() {
        return "phrase_form";
    }

    @RequestMapping(value = "/words/{id}", method = RequestMethod.GET)
    public String word_detail(Model model, @PathVariable(value = "id") Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        model.addAttribute("username", username);
        Boolean isAuthenticated;
        isAuthenticated = (!("anonymousUser".equals(username)));
        model.addAttribute("isAuthenticated", isAuthenticated);

        WordView wordView = new WordView();
        wordView = wordService.findWordViewByUsername(username, id);

        model.addAttribute("wordView", wordView);

        return "word_details";
    }
}
