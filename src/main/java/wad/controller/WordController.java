package wad.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wad.domain.Word;
import wad.service.PublicService;

@RestController
public class WordController {
    @Autowired
    private PublicService publicService;
    
    @RequestMapping("/words")
    public List<Word> words()
    {
        return publicService.findDefaultWords();
    }
}
