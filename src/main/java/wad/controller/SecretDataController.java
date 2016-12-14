package wad.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SecretDataController {

    @RequestMapping("/happypath")
    @ResponseBody
    public String happy() {
        return "Happy!";
    }

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
