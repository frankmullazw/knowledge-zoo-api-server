package edu.monash.knowledgezoo.api.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {

    // todo: Make it return the webpage
    @RequestMapping("/")
    @ResponseBody
    public String index() {
        return "This is the main mapping";
    }
}
