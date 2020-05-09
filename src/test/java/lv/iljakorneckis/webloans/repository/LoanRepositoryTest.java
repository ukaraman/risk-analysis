package lv.iljakorneckis.webloans.repository;

import lv.iljakorneckis.webloans.Launcher;
import lv.iljakorneckis.webloans.domain.Loan;
import lv.iljakorneckis.webloans.domain.LoanExtension;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static uk.co.it.modular.hamcrest.date.DateMatchers.after;
import static uk.co.it.modular.hamcrest.date.DateMatchers.within;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Launcher.class)
@Transactional
public class LoanRepositoryTest {

    private static final String USER_ID = "USER_ID";
    private static final Money AMOUNT = Money.of(CurrencyUnit.EUR, new BigDecimal("60.00"));
    private static final Integer LOAN_TERM = 5;
    private static final BigDecimal INTEREST = new BigDecimal("1.50");

    public Loan loan;

    @Autowired
    private LoanRepository loanRepo;

    @Before
    public void init() {
        loan = new Loan();
        loan.setUserId(USER_ID);
        loan.setAmount(AMOUNT);
        loan.setApplicationDate(DateTime.now());
        loan.setEndDate(DateTime.now().plusMonths(LOAN_TERM));
        loan.setInterest(INTEREST);
    }

    @After
    public void cleanUp() {
        loanRepo.deleteAll();
    }

    @Test
    public void testCreateLoan() {
        loan = loanRepo.save(loan);
        assertThat(loan.getId(), not(nullValue()));

        Loan savedLoan = loanRepo.findOne(loan.getId());

        assertThat(savedLoan.getAmount(), equalTo(AMOUNT));
        assertThat(savedLoan.getUserId(), equalTo(USER_ID));
        assertThat(savedLoan.getApplicationDate().toDate(), within(10, TimeUnit.SECONDS, DateTime.now().toDate()));
        assertThat(savedLoan.getEndDate().toDate(), allOf(within(31*LOAN_TERM, TimeUnit.DAYS, DateTime.now().toDate()),
                after(savedLoan.getApplicationDate().toDate())));
        assertThat(savedLoan.getInterest(), equalTo(INTEREST));
    }

    @Test
    public void testUpdateLoan() {
        BigDecimal newInterest = new BigDecimal("3.00");

        loanRepo.save(loan);

        Loan savedLoan = loanRepo.findOne(loan.getId());
        savedLoan.setInterest(newInterest);

        loanRepo.save(savedLoan);

        savedLoan = loanRepo.findOne(loan.getId());

        assertThat(savedLoan.getInterest(), equalTo(newInterest));
    }

    @Test
    public void testSaveExtensions() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        LoanExtension ext1 = new LoanExtension();
        ext1.setExtensionDate(DateTime.now());

        LoanExtension ext2 = new LoanExtension();
        ext2.setExtensionDate(DateTime.now().plusHours(1));

        loan.setExtensionHistory(Arrays.asList(ext1, ext2));

        loan = loanRepo.save(loan);

        Loan savedLoan = loanRepo.findOne(loan.getId());

        assertThat(savedLoan.getId(), not(nullValue()));
        assertThat(savedLoan.getExtensionHistory(), hasSize(2));

        for(LoanExtension ext : savedLoan.getExtensionHistory()) {
            assertThat(ext.getId(), not(nullValue()));
        }
    }

    @Test
    public void testGetLoansForUser() {
        loanRepo.save(loan);

        Loan anotherLoan = new Loan();
        anotherLoan.setUserId(USER_ID);
        anotherLoan.setApplicationDate(DateTime.now());
        anotherLoan.setEndDate(DateTime.now().plusDays(1));
        anotherLoan.setAmount(AMOUNT);

        loanRepo.save(anotherLoan);

        List<Loan> loans =  loanRepo.findByUserId(USER_ID);
        assertThat(loans, hasSize(2));
    }
}
