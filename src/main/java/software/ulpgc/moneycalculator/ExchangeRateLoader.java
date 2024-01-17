package software.ulpgc.moneycalculator;

import java.util.List;

public interface ExchangeRateLoader {
    List<ExchangeRate> load(Currency from, Currency to);
}
