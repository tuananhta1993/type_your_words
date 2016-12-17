package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecretDataController {
    @RequestMapping("/secretpath")
    @ResponseBody
    public String secret() {
        return "Secret!";
    }

    @RequestMapping("/adminpath")
    @ResponseBody
    public String admin() {
        return "Admin!";
    }

}
