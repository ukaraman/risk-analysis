package lv.iljakorneckis.webloans.service;

import lv.iljakorneckis.webloans.component.LoanRiskAssessorImpl;
import lv.iljakorneckis.webloans.component.settings.LoanDefaultSettings;
import lv.iljakorneckis.webloans.domain.Loan;
import lv.iljakorneckis.webloans.domain.LoanApplication;
import lv.iljakorneckis.webloans.domain.LoanExtension;
import lv.iljakorneckis.webloans.domain.LoanRiskAssessment;
import lv.iljakorneckis.webloans.enums.RiskStatus;
import lv.iljakorneckis.webloans.exceptions.RiskAssessmentException;
import lv.iljakorneckis.webloans.repository.LoanRepository;
import org.hamcrest.Matchers;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class LoanServiceImplTest {

    private static Money AMOUNT = Money.of(CurrencyUnit.EUR, new BigDecimal("500.00"));
    private static BigDecimal INTEREST = new BigDecimal("5.00");
    private static BigDecimal FACTOR = new BigDecimal("1.50");


    @Mock
    private LoanDefaultSettings settings;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private LoanRiskAssessorImpl loanRiskAssessor;

    @InjectMocks
    private LoanServiceImpl loanService;

    @Before
    public void init() {
        when(settings.getFactor()).thenReturn(FACTOR);
        when(settings.getInterest()).thenReturn(INTEREST);
        when(settings.getWeekIncreasePerExtension()).thenReturn(1);
    }

    @Test
    public void testApplyForLoanTest() throws RiskAssessmentException {
        LoanApplication loanApplication = mock(LoanApplication.class);
        when(loanApplication.getUserId()).thenReturn("USER_ID");
        when(loanApplication.getApplicationDate()).thenReturn(DateTime.now());
        when(loanApplication.getAmount()).thenReturn(AMOUNT);

        LoanRiskAssessment assessment = mock(LoanRiskAssessment.class);
        when(assessment.getStatus()).thenReturn(RiskStatus.OK);
        when(assessment.getMessage()).thenReturn("OK MESSAGE");

        when(loanRiskAssessor.assessRisk(loanApplication)).thenReturn(assessment);

        loanService.applyForLoan(loanApplication);
        verify(loanRiskAssessor, times(1)).assessRisk(loanApplication);
        verify(loanRepository, times(1)).save(argThat(Matchers.any(Loan.class)));
    }

    @Test(expected = RiskAssessmentException.class)
    public void testApplyForLoanAfterMidnight() throws RiskAssessmentException {
        LoanApplication application = mock(LoanApplication.class);

        LoanRiskAssessment assessment = mock(LoanRiskAssessment.class);
        when(assessment.getStatus()).thenReturn(RiskStatus.APPLIED_AFTER_MIDNIGHT);
        when(assessment.getMessage()).thenReturn("Suspicious application!");

        when(loanRiskAssessor.assessRisk(application)).thenReturn(assessment);

        loanService.applyForLoan(application);
    }

    @Test(expected = RiskAssessmentException.class)
    public void testApplyForLoanMaxApplications() throws RiskAssessmentException {
        LoanApplication application = mock(LoanApplication.class);

        LoanRiskAssessment assessment = mock(LoanRiskAssessment.class);
        when(assessment.getStatus()).thenReturn(RiskStatus.TOO_MANY_APPLICATIONS);
        when(assessment.getMessage()).thenReturn("Max number of applications reached!");

        when(loanRiskAssessor.assessRisk(application)).thenReturn(assessment);
        loanService.applyForLoan(application);
    }

    @Test
    public void testExtendLoan() {

        Loan loan = mock(Loan.class);
        List<LoanExtension> extensionHistory = mock(ArrayList.class);
        when(loan.getExtensionHistory()).thenReturn(extensionHistory);

        Long id = 12345L;
        when(loan.getId()).thenReturn(id);
        when(loan.getAmount()).thenReturn(AMOUNT);
        when(loan.getApplicationDate()).thenReturn(DateTime.now());

        DateTime endDate = DateTime.now().plusMonths(1);
        when(loan.getEndDate()).thenReturn(endDate);

        when(loan.getInterest()).thenReturn(INTEREST);

        String userId = "USER_ID";
        when(loan.getUserId()).thenReturn(userId);

        when(loanRepository.findByIdAndUserId(anyLong(), anyString())).thenReturn(loan);

        loanService.extendLoan(id, userId);

        verify(loan, times(1)).setInterest(INTEREST.multiply(FACTOR));
        verify(loan, times(1)).setEndDate(endDate.plusWeeks(1));
        verify(loan, times(1)).getExtensionHistory();
        verify(extensionHistory, times(1)).add(argThat(Matchers.any(LoanExtension.class)));
    }

    @Test
    public void testGetLoanHistory() {
        String userId = "TEST_USER_ID";
        loanService.getLoanHistory(userId);

        verify(loanRepository, times(1)).findByUserId(userId);
    }
}