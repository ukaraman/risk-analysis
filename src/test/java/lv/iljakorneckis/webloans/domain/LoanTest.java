package lv.iljakorneckis.webloans.domain;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static uk.co.it.modular.hamcrest.date.IsWithin.within;

public class LoanTest {

    private Loan loan;

    @Before
    public void init() {
        loan = new Loan();
    }

    @Test
    public void testGetLoanAmountAndCurrency() {
        loan.setAmount(null);
        assertThat(loan.getLoanAmount(), nullValue());
        assertThat(loan.getCurrency(), nullValue());

        BigDecimal amount = new BigDecimal("500.00");
        loan.setAmount(Money.of(CurrencyUnit.EUR, amount));
        assertThat(loan.getLoanAmount(), equalTo(amount));
        assertThat(loan.getCurrency(), equalTo(CurrencyUnit.EUR.getCurrencyCode()));
    }

    @Test
    public void testGetDates() {
        loan.setApplicationDate(null);
        loan.setEndDate(null);

        assertThat(loan.getLoanDate(), nullValue());
        assertThat(loan.getEndDate(), nullValue());

        DateTime applicationDate = DateTime.now();
        loan.setApplicationDate(applicationDate);
        DateTime endDate = applicationDate.plusWeeks(2);
        loan.setEndDate(endDate);

        assertThat(loan.getLoanDate(), within(2, TimeUnit.SECONDS, applicationDate.toDate()));
        assertThat(loan.getTermDate(), within(2, TimeUnit.SECONDS, endDate.toDate()));
    }
}
