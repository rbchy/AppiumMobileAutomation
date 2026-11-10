package utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses the app's price labels ("$ 29.99", "$7.99", "$ 1,049.00") and checks ordering. */
public final class Prices {

    private static final Pattern AMOUNT = Pattern.compile("(\\d{1,3}(?:,\\d{3})+|\\d+)(?:\\.(\\d+))?");

    private Prices() {}

    public static BigDecimal parse(String label) {
        if (label == null) throw new IllegalArgumentException("Price label is null");
        Matcher m = AMOUNT.matcher(label);
        if (!m.find()) throw new IllegalArgumentException("No amount in price label: '" + label + "'");
        String whole = m.group(1).replace(",", "");
        String frac = m.group(2);
        return new BigDecimal(frac == null ? whole : whole + "." + frac);
    }

    public static List<BigDecimal> parseAll(List<String> labels) {
        List<BigDecimal> out = new ArrayList<>();
        for (String l : labels) out.add(parse(l));
        return out;
    }

    public static boolean isAscending(List<BigDecimal> values) {
        for (int i = 1; i < values.size(); i++)
            if (values.get(i - 1).compareTo(values.get(i)) > 0) return false;
        return true;
    }

    public static boolean isDescending(List<BigDecimal> values) {
        for (int i = 1; i < values.size(); i++)
            if (values.get(i - 1).compareTo(values.get(i)) < 0) return false;
        return true;
    }
}
