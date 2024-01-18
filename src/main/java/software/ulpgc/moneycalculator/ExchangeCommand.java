package software.ulpgc.moneycalculator;

import software.ulpgc.moneycalculator.fixerws.FixerExchangeRateLoader;

import javax.swing.*;
import java.util.List;

public class ExchangeCommand implements Command {

    private final JComboBox<Currency> fromCurrency;
    private final JComboBox<Currency> toCurrency;
    private final JTextField fromQuantity;
    private final JTextField toQuantity;
    private double quantity;
    private List<ExchangeRate> exchangeRates;
    private Currency from;
    private Currency to;

    public ExchangeCommand(JComboBox<Currency> fromCurrency, JComboBox<Currency> toCurrency, JTextField fromQuantity, JTextField toQuantity) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.fromQuantity = fromQuantity;
        this.toQuantity = toQuantity;
    }

    @Override
    public void execute() {
        try {
            read();
            double euros = quantity / FixerExchangeRateLoader.find(exchangeRates, from).rate();
            double result = euros * FixerExchangeRateLoader.find(exchangeRates, to).rate();
            toQuantity.setText(Double.toString(result));
        } catch (Exception ex) {
            System.out.printf("Error during conversion: " + ex.getMessage());
        }
    }

    public void read() {
        quantity = Double.parseDouble(fromQuantity.getText());
        from = (Currency) fromCurrency.getSelectedItem();
        to = (Currency) toCurrency.getSelectedItem();
        FixerExchangeRateLoader exchangeRateLoader = new FixerExchangeRateLoader();
        exchangeRates = exchangeRateLoader.load(from, to);
    }
}
