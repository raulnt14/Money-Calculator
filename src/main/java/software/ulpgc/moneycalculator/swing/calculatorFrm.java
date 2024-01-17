package software.ulpgc.moneycalculator.swing;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import software.ulpgc.moneycalculator.Currency;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;

public class calculatorFrm extends JFrame {
    private static final String key = "31547d8fcf138f06d26b4f66076050d1";

    private JPanel panel1;
    private JComboBox<Currency> fromCurrency;
    private JComboBox<Currency> toCurrency;
    private JTextField fromQuantity;
    private JTextField toQuantity;
    private JButton calculate;

    private final List<Currency> currencies;

    public calculatorFrm() throws MalformedURLException {
        URL url = new URL("http://data.fixer.io/api/symbols?access_key=" + key);
        currencies = toList(url);
        this.createUIComponents();
    }

    private List<Currency> toList(URL url) {
        try {
            InputStream is = url.openStream();
            List<Currency> list = new ArrayList<>();
            String json = new String(is.readAllBytes());
            Map<String, JsonElement> symbols = new Gson().fromJson(json, JsonObject.class).get("symbols").getAsJsonObject().asMap();
            for (String symbol : symbols.keySet())
                list.add(new Currency(symbol, symbols.get(symbol).getAsString()));
            return list;
        } catch (IOException e) {
            return emptyList();
        }
    }

    private void createUIComponents() {
        setContentPane(panel1);
        setSize(600, 300);
        setResizable(false);
        for (Currency currency : currencies) fromCurrency.addItem(currency);
        for (Currency currency : currencies) toCurrency.addItem(currency);
    }
}
