package software.ulpgc.moneycalculator.fixerws;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.ExchangeRate;
import software.ulpgc.moneycalculator.ExchangeRateLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class FixerExchangeRateLoader implements ExchangeRateLoader {
    @Override
    public List<ExchangeRate> load(Currency from, Currency to) {
        try {
            return toList(loadJson(from ,to));
        } catch (IOException e) {
            return emptyList();
        }
    }

    private List<ExchangeRate> toList(String json) {
        List<ExchangeRate> list = new ArrayList<>();
        Map<String, JsonElement> rates = new Gson().fromJson(json, JsonObject.class).get("rates").getAsJsonObject().asMap();
        for (String currency : rates.keySet())
            list.add(new ExchangeRate(currency, rates.get(currency).getAsDouble()));
        return list;
    }

    public static ExchangeRate find(List<ExchangeRate> list, Currency currency) throws Exception {
        for (ExchangeRate er : list) {
            if (er.to().equals(currency.code())) {
                return er;
            }
        }
        throw new Exception("Exchange rate unavailable");
    }

    private String loadJson(Currency from, Currency to) throws IOException {
        URL url = new URL(String.format("http://data.fixer.io/api/latest?access_key=%s&base=EUR&symbols=%s,%s", FixerAPI.key, from.code(), to.code()));
        try (InputStream is = url.openStream()) {
            return new String(is.readAllBytes());
        }
    }
}
