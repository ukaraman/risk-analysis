package lv.iljakorneckis.webloans.component.settings;

import org.joda.money.Money;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "loan.assessment")
public class LoanRiskAssessmentSettings {

    @NotNull
    private Money maxAmount;

    @NotNull
    private Integer maxLoansPerDay;

    @NotNull
    private Integer morningHour;

    public Money getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Money maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Integer getMaxLoansPerDay() {
        return maxLoansPerDay;
    }

    public void setMaxLoansPerDay(Integer maxLoansPerDay) {
        this.maxLoansPerDay = maxLoansPerDay;
    }

    public Integer getMorningHour() {
        return morningHour;
    }

    public void setMorningHour(Integer morningHour) {
        this.morningHour = morningHour;
    }
}
