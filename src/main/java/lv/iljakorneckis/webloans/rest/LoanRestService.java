package lv.iljakorneckis.webloans.rest;

import lv.iljakorneckis.webloans.component.producer.DateTimeProducer;
import lv.iljakorneckis.webloans.domain.Loan;
import lv.iljakorneckis.webloans.domain.LoanApplication;
import lv.iljakorneckis.webloans.exceptions.RiskAssessmentException;
import lv.iljakorneckis.webloans.service.LoanService;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(value = "/rest/loans")
public class LoanRestService {

    @Autowired
    private DateTimeProducer dateTimeProducer;

    @Autowired
    private LoanService loanService;

    @RequestMapping(method = RequestMethod.GET)
    public List<Loan> getLoanHistory(HttpServletRequest request) {
        final String ip = request.getRemoteAddr();

        return loanService.getLoanHistory(ip);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> applyForLoan(@RequestParam("amount") BigDecimal amount,
                                               @RequestParam("term") Integer term,
                                               HttpServletRequest request) throws RiskAssessmentException {

        final String ip = request.getRemoteAddr();

        LoanApplication application = new LoanApplication();
        application.setAmount(Money.of(CurrencyUnit.EUR, amount));
        application.setApplicationDate(dateTimeProducer.getCurrentDateTime());
        application.setTerm(term);
        application.setUserId(ip);

        loanService.applyForLoan(application);

        return new ResponseEntity<String>(HttpStatus.OK);
    }

    @RequestMapping(value="/{loanId}/extend", method = RequestMethod.POST)
    public ResponseEntity<String> extendLoan(@PathVariable("loanId") Long loanId,
                           HttpServletRequest request) {
        final String ip = request.getRemoteAddr();
        loanService.extendLoan(loanId, ip);

        return new ResponseEntity<String>(HttpStatus.OK);
    }
}