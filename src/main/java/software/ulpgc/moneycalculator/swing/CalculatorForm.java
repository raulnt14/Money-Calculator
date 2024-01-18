package software.ulpgc.moneycalculator.swing;

import software.ulpgc.moneycalculator.Command;
import software.ulpgc.moneycalculator.Currency;
import software.ulpgc.moneycalculator.ExchangeCommand;
import software.ulpgc.moneycalculator.fixerws.FixerCurrencyLoader;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculatorForm extends JFrame {
    private JPanel panel1;
    private JComboBox<Currency> fromCurrency;
    private JComboBox<Currency> toCurrency;
    private JTextField fromQuantity;
    private JTextField toQuantity;
    private JButton calculate;
    private final List<Currency> currencies;
    private final Map<String, Command> commands = new HashMap<>();

    public CalculatorForm() {
        FixerCurrencyLoader currencyLoader = new FixerCurrencyLoader();
        currencies = currencyLoader.load();
        this.createUIComponents();
        Command command = new ExchangeCommand(fromCurrency, toCurrency, fromQuantity, toQuantity);
        add("exchange money", command);
        calculate.addActionListener(e -> commands.get("exchange money").execute());
    }

    private void createUIComponents() {
        setTitle("Money Calculator");
        setSize(600, 300);
        setContentPane(panel1);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        for (Currency currency : currencies) fromCurrency.addItem(currency);
        for (Currency currency : currencies) toCurrency.addItem(currency);
    }

    private void add(String name, Command command) {
        commands.put(name, command);
    }
}
