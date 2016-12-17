package wad.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
import wad.repository.WordDetailRepository;
import wad.repository.WordRepository;

@Controller
public class WordDetailController {

    @Autowired
    WordDetailRepository wdRepository;

    @Autowired
    WordRepository wordRepository;

    @RequestMapping(value = "/wordDetails/create/{id}", method = RequestMethod.GET)
    public String createForm(@PathVariable Long id, Model model) {
        System.out.println("ID=" + id.toString());

        Word word = wordRepository.findOne(id);
        WordDetail wordDetail = new WordDetail();

        if (word == null) {
            word = new Word();
        }

        model.addAttribute("word", word);
        model.addAttribute("wordDetail", wordDetail);

        return "word_definition_form";
    }

    @RequestMapping(value = "/wordDetails/create/{wordId}", method = RequestMethod.POST)
    public String createDetail(
            @PathVariable Long wordId,
            @Valid
            @ModelAttribute WordDetail wordDetail,
            BindingResult bindingResult,
            HttpServletResponse response) throws IOException {
        System.out.println(wordId.toString());

        if (bindingResult.hasErrors()) {
            return "word_definition_form";
        }

        Word word = wordRepository.findOne(wordId);
        wordDetail.setWord(word);
        wdRepository.save(wordDetail);

        PrintWriter out = response.getWriter();
        out.println("redirect:/words/" + word.getId().toString());

        return null;
    }
}
