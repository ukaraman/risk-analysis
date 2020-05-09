package lv.iljakorneckis.webloans.rest;

import lv.iljakorneckis.webloans.component.producer.DateTimeProducer;
import lv.iljakorneckis.webloans.domain.Loan;
import lv.iljakorneckis.webloans.domain.LoanApplication;
import lv.iljakorneckis.webloans.exceptions.RiskAssessmentException;
import lv.iljakorneckis.webloans.service.LoanService;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoanRestServiceTest {

    private static final String USER_IP = "127.0.0.1";

    @Mock
    private HttpServletRequest request;

    @Mock
    private LoanService loanService;

    @Mock
    private DateTimeProducer dateTimeProducer;

    @InjectMocks
    private LoanRestService restService;

    @Before
    public void init() {
        when(request.getRemoteAddr()).thenReturn(USER_IP);
    }

    @Test
    public void testGetLoanHistory() {
        Loan mockLoan = mock(Loan.class);
        List<Loan> list = Arrays.asList(mockLoan, mockLoan);
        when(loanService.getLoanHistory(USER_IP)).thenReturn(list);

        List<Loan> userLoans = restService.getLoanHistory(request);

        verify(loanService, times(1)).getLoanHistory(USER_IP);
        assertThat(userLoans, hasSize(2));
    }

    @Test
    public void testApplyForLoan() throws RiskAssessmentException {
        BigDecimal amount = new BigDecimal("123.00");
        Integer term = 31;

        when(dateTimeProducer.getCurrentDateTime()).thenReturn(DateTime.now().withHourOfDay(13));

        restService.applyForLoan(amount, term, request);
        verify(loanService, times(1)).applyForLoan(any(LoanApplication.class));
    }

    @Test
    public void testExtendLoan() {
        Long loanId = 1234L;

        restService.extendLoan(loanId, request);
        verify(loanService, times(1)).extendLoan(loanId, USER_IP);
    }
}