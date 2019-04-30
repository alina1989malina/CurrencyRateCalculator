package pack.currency.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pack.currency.service.CurrencyService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping(value = "/getProfit", produces = MediaType.APPLICATION_JSON_VALUE)
    //Todo: params validation?
    public Long getProfit() {
        BigDecimal profit = currencyService.calcProfit(new BigDecimal(2000), "2019-04-20");//convertCurrency("USD", "RUB", 2000L, new Date());
        System.out.println(profit);
        return 0L;
    }

//    @GetMapping(value = "/getProfit", produces = MediaType.APPLICATION_JSON_VALUE)
//    //Todo: params validation?
//    public Long getProfit(@RequestParam(name = "amount") Long moneyAmountInUsd, @RequestParam(name = "date") Date dateOfPutchase) {
//        currencyService.convertCurrency(String startCurrencyName, String endCurrencyName, Long amount, Date date)
//        return 0L;
//    }
}
