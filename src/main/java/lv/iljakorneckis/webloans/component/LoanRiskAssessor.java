package lv.iljakorneckis.webloans.component;

import lv.iljakorneckis.webloans.domain.LoanApplication;
import lv.iljakorneckis.webloans.domain.LoanRiskAssessment;

public interface LoanRiskAssessor {
    LoanRiskAssessment assessRisk(LoanApplication loanApplication);
}
