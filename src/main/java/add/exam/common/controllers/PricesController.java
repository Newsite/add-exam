package add.exam.common.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping (PricesController.REQUEST_URL)
public class PricesController
{
    public static final String REQUEST_URL = "/prices";

    //templates
    private static final String PRICES_TEMPLATE = "common/prices";

    @RequestMapping(method = RequestMethod.GET)
    public String showPrices(){
        //TODO: add showButtons attribute (only for users)
        return PRICES_TEMPLATE;
    }
}
