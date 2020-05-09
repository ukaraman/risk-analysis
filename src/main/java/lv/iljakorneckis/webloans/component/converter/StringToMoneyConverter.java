package lv.iljakorneckis.webloans.component.converter;

import org.joda.money.Money;
import org.springframework.core.convert.converter.Converter;

public class StringToMoneyConverter implements Converter<String, Money> {
    @Override
    public Money convert(String moneyString) {
        return Money.parse(moneyString);
    }
}
