package com.financontrol.wicket.converter;

import org.apache.wicket.util.convert.IConverter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class MoneyConverter implements IConverter<BigDecimal> {

    @Override
    public BigDecimal convertToObject(String value, Locale locale) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            // Remove "R$", spaces, and dots. Replace comma with dot.
            // Example: "R$ 1.234,56" -> "1234.56"
            String cleanValue = value
                    .replace("R$", "")
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".");
            return new BigDecimal(cleanValue.trim());
        } catch (NumberFormatException e) {
            throw new org.apache.wicket.util.convert.ConversionException("Invalid currency format", e);
        }
    }

    @Override
    public String convertToString(BigDecimal value, Locale locale) {
        if (value == null) {
            return "";
        }
        // Format as 1.234,56
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
        symbols.setDecimalSeparator(',');
        symbols.setGroupingSeparator('.');

        DecimalFormat df = new DecimalFormat("#,##0.00", symbols);
        return df.format(value);
    }
}
