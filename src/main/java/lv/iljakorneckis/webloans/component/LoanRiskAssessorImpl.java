package lv.iljakorneckis.webloans.component;

import lv.iljakorneckis.webloans.component.producer.DateTimeProducer;
import lv.iljakorneckis.webloans.component.settings.LoanRiskAssessmentSettings;
import lv.iljakorneckis.webloans.domain.Loan;
import lv.iljakorneckis.webloans.domain.LoanApplication;
import lv.iljakorneckis.webloans.domain.LoanRiskAssessment;
import lv.iljakorneckis.webloans.enums.RiskStatus;
import lv.iljakorneckis.webloans.repository.LoanRepository;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanRiskAssessorImpl implements LoanRiskAssessor {

    @Autowired
    private DateTimeProducer dateTimeProducer;

    @Autowired
    private LoanRiskAssessmentSettings settings;

    @Autowired
    private LoanRepository loanRepo;

    @Override
    public LoanRiskAssessment assessRisk(LoanApplication loanApplication) {
        List<Loan> loanList = loanRepo.findByUserId(loanApplication.getUserId());

        if(settings.getMaxAmount().isLessThan(loanApplication.getAmount())) {
            return new LoanRiskAssessment(RiskStatus.MAX_AMOUNT_EXCEEDED, "Maximum loan amount exceeded");
        }

        if(isMaxNumberOfApplicationsPerDay(loanList, settings.getMaxLoansPerDay())) {
            return new LoanRiskAssessment(RiskStatus.TOO_MANY_APPLICATIONS, "Maximum number of loans reached");
        }

        // If application is made at night with max possible amount
        if(isNightTime(loanApplication.getApplicationDate()) && settings.getMaxAmount().isEqual(loanApplication.getAmount())) {
            return new LoanRiskAssessment(RiskStatus.APPLIED_AFTER_MIDNIGHT, "Application made after midnight with maximum allowed amount");
        }

        return new LoanRiskAssessment(RiskStatus.OK, "Loan risk is acceptable");
    }


    private boolean isNightTime(DateTime applicationDate) {
        LocalDateTime localApplicationDate = applicationDate.toLocalDateTime();
        return localApplicationDate.getHourOfDay() < settings.getMorningHour();
    }

    private boolean isMaxNumberOfApplicationsPerDay(List<Loan> userLoans, Integer maxNumberPerDay) {
        LocalDate today = dateTimeProducer.getCurrentDateTime().toLocalDate();

        int numberOfApplicationsPerToday = 0;

        for(Loan loan : userLoans) {
            LocalDate loanDate = loan.getApplicationDate().toLocalDate();

            if(today.compareTo(loanDate) == 0) {
                numberOfApplicationsPerToday += 1;
            }
        }

        return numberOfApplicationsPerToday == maxNumberPerDay;
    }
}