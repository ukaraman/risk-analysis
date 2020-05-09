package lv.iljakorneckis.webloans.domain;

import lv.iljakorneckis.webloans.enums.RiskStatus;

public class LoanRiskAssessment {

    private RiskStatus status;
    private String message;

    public LoanRiskAssessment(RiskStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public RiskStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}