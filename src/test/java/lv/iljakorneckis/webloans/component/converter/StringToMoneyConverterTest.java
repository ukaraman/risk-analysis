package lv.iljakorneckis.webloans.component.converter;


import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class StringToMoneyConverterTest {

    private StringToMoneyConverter converter;

    @Before
    public void init() {
        converter = new StringToMoneyConverter();
    }

    @Test
    public void testConvert() {
        Money eur = converter.convert("EUR 1123.12");
        assertThat(eur.getCurrencyUnit(), equalTo(CurrencyUnit.EUR));
        assertThat(eur.getAmount(), equalTo(new BigDecimal("1123.12")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalInput() {
        converter.convert("");
    }

}