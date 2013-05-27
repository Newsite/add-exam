package add.exam.common.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(MathSymbolsController.REQUEST_URL)
public class MathSymbolsController
{
    public static final String REQUEST_URL = "/math-symbols";

    //templates
    private static final String MATH_SYMBOLS_TEMPLATE = "common/math-symbols";

    @RequestMapping(method = RequestMethod.GET)
    public String showMathSymbols(){
        return MATH_SYMBOLS_TEMPLATE;
    }

}
