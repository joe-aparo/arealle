package net.jsa.arealle.web.controller;

import java.util.LinkedHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("arealle")
public class MainController {
	
    /**
     * Direct to single page main view
     * 
     * @return Main view
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView getMainApp() {
        return new ModelAndView("main", new LinkedHashMap<String, String>());
    }
}
