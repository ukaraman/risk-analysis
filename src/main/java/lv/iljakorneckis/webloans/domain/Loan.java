package lv.iljakorneckis.webloans.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Columns;
import org.hibernate.annotations.Type;
import org.joda.money.Money;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@JsonIgnoreProperties({"amount", "applicationDate", "endDate"})
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Columns(columns = { @Column(name = "CURRENCY"), @Column(name = "AMOUNT") })
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmountAndCurrency")
    private Money amount;

    private BigDecimal interest;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime applicationDate;

    @Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    private DateTime endDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoanExtension> extensionHistory;

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Money getAmount() {
        return amount;
    }

    /**
     * Synthetic field, exposes decimal part of {@link org.joda.money.Money} getAmount field for Json serialization
     */
    public BigDecimal getLoanAmount() {
        if(amount != null) {
            return amount.getAmount();
        }

        return null;
    }

    /**
     * Synthetic field, exposes {@link org.joda.money.CurrencyUnit} part of getAmount field
     */
    public String getCurrency() {
        if(amount != null) {
            return amount.getCurrencyUnit().getCurrencyCode();
        }

        return null;
    }

    public void setAmount(Money amount) {
        this.amount = amount;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public DateTime getApplicationDate() {
        return applicationDate;
    }

    /**
     * Synthetic field, exposes <b>applicationDate</b> as {@link java.util.Date} for serialization
     */
    public Date getLoanDate() {
        if(applicationDate != null) {
            return applicationDate.toDate();
        }

        return null;
    }

    public void setApplicationDate(DateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public DateTime getEndDate() {
        return endDate;
    }

    /**
     * Synthetic field, exposes <b>endDate</b> as {@link java.util.Date} for serialization
     */
    public Date getTermDate() {
        if(endDate != null) {
            return endDate.toDate();
        }

        return null;
    }

    public void setEndDate(DateTime endDate) {
        this.endDate = endDate;
    }

    public List<LoanExtension> getExtensionHistory() {
        return extensionHistory;
    }

    public void setExtensionHistory(List<LoanExtension> extensionHistory) {
        this.extensionHistory = extensionHistory;
    }
}
