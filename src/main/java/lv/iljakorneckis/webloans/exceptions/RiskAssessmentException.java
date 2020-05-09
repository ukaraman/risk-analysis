package lv.iljakorneckis.webloans.exceptions;

import lv.iljakorneckis.webloans.enums.RiskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class RiskAssessmentException extends Exception {
    private RiskStatus status;

    public RiskAssessmentException(RiskStatus status, String message) {
        super(message);
        this.status = status;
    }

    public RiskStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return String.format("%s: %s", status, super.getMessage());
    }
}
