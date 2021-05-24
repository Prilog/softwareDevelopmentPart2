package Application;

import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class LocalCurrency {
    private static final Map<String, Map<String, Double>> convertion = new HashMap<String, Map<String, Double>>() {{
        put("RUB", new HashMap<String, Double>() {{
            put("RUB", 1.0);
            put("USD", 0.014);
            put("JPY", 2.4);
        }});
        put("USD", new HashMap<String, Double>() {{
            put("RUB", 70.0);
            put("USD", 1.0);
            put("JPY", 175.0);
        }});
        put("JPY", new HashMap<String, Double>() {{
            put("RUB", 0.4);
            put("USD", 0.005);
            put("JPY", 1.0);
        }});
    }};
    Currency realCurrency;
    double amount;

    public LocalCurrency(Currency c, double a) {
        amount = a;
        realCurrency = c;
    }

    public static boolean isLegal(String c) {
        return c.equals("RUB") || c.equals("USD") || c.equals("JPY");
    }

    public double getAmount() {
        return amount;
    }

    public LocalCurrency convertTo(Currency newCurrency) {
        if (convertion.containsKey(newCurrency.toString())) {
            return new LocalCurrency(newCurrency, amount * convertion.get(realCurrency.toString()).get(newCurrency.toString()));
        }
        throw new IllegalArgumentException();
    }

    @Override
    public String toString() {
        return amount + realCurrency.getSymbol();
    }

    public Currency getRealCurrency() {
        return realCurrency;
    }

}
