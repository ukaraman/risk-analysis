package lv.iljakorneckis.webloans.service;

import lv.iljakorneckis.webloans.domain.Loan;
import lv.iljakorneckis.webloans.domain.LoanApplication;
import lv.iljakorneckis.webloans.exceptions.RiskAssessmentException;

import java.util.List;

public interface LoanService {

    Loan applyForLoan(LoanApplication application) throws RiskAssessmentException;

    Loan extendLoan(Long loanId, String userId);

    List<Loan> getLoanHistory(String userId);
}
