package software.ulpgc.moneycalculator.swing;

import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.ExchangeRate;
import software.ulpgc.moneycalculator.fixerws.FixerCurrencyLoader;
import software.ulpgc.moneycalculator.fixerws.FixerExchangeRateLoader;

import javax.swing.*;
import java.util.List;

public class CalculatorForm extends JFrame {
    private JPanel panel1;
    private JComboBox<Currency> fromCurrency;
    private JComboBox<Currency> toCurrency;
    private JTextField fromQuantity;
    private JTextField toQuantity;
    private JButton calculate;

    private final List<Currency> currencies;

    public CalculatorForm() {
        FixerCurrencyLoader currencyLoader = new FixerCurrencyLoader();
        currencies = currencyLoader.load();
        this.createUIComponents();
        calculate.addActionListener(e -> convert());
    }

    private void convert() {
        try {
            double quantity = Double.parseDouble(fromQuantity.getText());
            Currency from = (Currency) fromCurrency.getSelectedItem();
            Currency to = (Currency) toCurrency.getSelectedItem();
            FixerExchangeRateLoader exchangeRateLoader = new FixerExchangeRateLoader();
            List<ExchangeRate> exchangeRates = exchangeRateLoader.load(from, to);
            double euros = quantity / FixerExchangeRateLoader.find(exchangeRates, from).rate();
            double result = euros * FixerExchangeRateLoader.find(exchangeRates, to).rate();
            toQuantity.setText(Double.toString(result));
        } catch (Exception ex) {
            System.out.printf("Error during convertion: " + ex.getMessage());
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
