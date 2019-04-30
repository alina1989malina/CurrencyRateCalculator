package pack.currency.service;

import java.math.BigDecimal;
import java.util.Date;

public interface CurrencyService {
//    Long convertCurrency(String startCurrencyName, String endCurrencyName, Long amount, Date date);
public BigDecimal calcProfit(BigDecimal amount, String date);
}
