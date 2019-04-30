package pack.currency.service.impl;

import com.google.common.io.CharStreams;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pack.currency.cache.CacheHelper;
import pack.currency.service.CurrencyService;


import javax.net.ssl.*;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class CurrencyServiceImpl implements CurrencyService{
    private static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final String CURRENCY_URL_USD_TO_RUB_FOR_CURRENT_DATE = "http://api.exchangeratesapi.io/latest?base=USD&symbols=RUB";
    private static final String CURRENCY_URL_USD_TO_RUB_BASE= "http://api.exchangeratesapi.io";
    private static final BigDecimal SPREAD = new BigDecimal(0.64);

    @Autowired
    private CacheHelper cacheHelper;

//    public Long convertCurrency(String startCurrencyName, String endCurrencyName, Long amount, Date date){
//        String urlStr = getCurrencyUrlForGivenDate(SIMPLE_DATE_FORMAT.format(date));
//        System.out.println(urlStr);
//        String response = getResponseFromHttpUrl(urlStr);
//        System.out.println(response);
//        readUsdPriceInRub(response);
//        System.out.println(readUsdPriceInRub(response));
//        return 0L;
//    }

    public BigDecimal calcProfit(BigDecimal amount, String date){
//        try {
//          //  System.out.println(getResponseFromHttpUrl(CURRENCY_URL_USD_TO_RUB_FOR_CURRENT_DATE));
//            System.out.println(getCurrencyUrlForGivenDate(date));
//           /
//            System.out.println("res = " + getResponseFromHttpUrl("https://api.exchangeratesapi.io/2019-04-29?base=USD&symbols=RUB"));
//            //BigDecimal curSum = computeCurrentMoneyAmountInRub(amount);
//           // System.out.println(curSum);
//        }
//        catch (Exception e){
//
//        }


        return /**/computeCurrentMoneyAmountInRub(amount).subtract(computeMoneyAmountInRubForGivenDate(amount, date));
    }

    private BigDecimal computeMoneyAmountInRubForGivenDate(BigDecimal moneyAmountInUsd, String date){
//        String formattedDate = SIMPLE_DATE_FORMAT.format(date);
//        System.out.println(formattedDate);
        System.out.println(getCurrencyUrlForGivenDate(date));
        String response = getResponseFromHttpUrl(getCurrencyUrlForGivenDate(date));
        System.out.println(response);
        BigDecimal price = getUsdPriceInRub(response);
        return moneyAmountInUsd.multiply(price);
    }

    private BigDecimal computeCurrentMoneyAmountInRub(BigDecimal moneyAmountInUsd){
        String response = getResponseFromHttpUrl(CURRENCY_URL_USD_TO_RUB_FOR_CURRENT_DATE);
        System.out.println(response);
        BigDecimal price = getUsdPriceInRub(response);
        return moneyAmountInUsd.multiply(price.subtract(SPREAD));
    }

    // {"base":"USD","rates":{"RUB":64.7741848558},"date":"2019-04-26"}
    private BigDecimal getUsdPriceInRub(String jsonData){
        BigDecimal price = new BigDecimal(0);
        try{
            JSONObject jsonObj = new JSONObject(jsonData);
            JSONObject rates = (JSONObject) jsonObj.get("rates");
            price = rates.getBigDecimal("RUB");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return price;
    }


    private String getResponseFromHttpUrl(String urlStr)  {
        String res = "";
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = getHttpUrlConnection(urlStr);
            if(urlConnection == null || urlConnection.getResponseCode()!= HttpURLConnection.HTTP_OK) return res;
            res = CharStreams.toString(new InputStreamReader(urlConnection.getInputStream(), Charset.forName("UTF-8")));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
        return res;
    }

    private HttpURLConnection getHttpUrlConnection(String urlStr) throws Exception{
       // HttpURLConnection.setFollowRedirects(false);
        HttpURLConnection urlConnection;
        URL url = new URL(urlStr);
        urlConnection = (HttpURLConnection) url.openConnection();
        String redirect = urlConnection .getHeaderField("Location");
        if (redirect != null){
            urlConnection  = (HttpURLConnection) new URL(redirect).openConnection();
        }

        if (urlConnection == null) throw new Exception("Connection is null!");

        urlConnection.setConnectTimeout(1000000000);
        urlConnection.setReadTimeout(1000000000);
        urlConnection.connect();
        return urlConnection;
    }


    private String getCurrencyUrlForGivenDate(String date){
        return new StringBuilder().append(CURRENCY_URL_USD_TO_RUB_BASE)
                .append("/").append(date).append("?")
                .append("base=USD&symbols=RUB")
                .toString();
    }
}
