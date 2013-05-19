package add.exam.common.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(HomeController.REQUEST_MAPPING)
public class HomeController
{
    public static final String REQUEST_MAPPING = "/home";


    @RequestMapping(method = RequestMethod.GET)
    public String home(){

        return "home";
    }
}
