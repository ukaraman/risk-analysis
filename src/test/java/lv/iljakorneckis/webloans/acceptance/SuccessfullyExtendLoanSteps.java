package lv.iljakorneckis.webloans.acceptance;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lv.iljakorneckis.webloans.helper.TestHelper;
import org.joda.time.DateTime;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static com.jayway.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static uk.co.it.modular.hamcrest.date.DateMatchers.within;

public class SuccessfullyExtendLoanSteps {

    private String loanId;
    private BigDecimal initialInterest;
    private DateTime initialEndDate;

    @Given("^user has a loan registered$")
    public void user_has_a_loan_registered() {
        String loanJson = get("/rest/loans").asString();
        this.loanId = from(loanJson).getString("[0].id");
        this.initialInterest = new BigDecimal(from(loanJson).getString("[0].interest"));
        this.initialEndDate = new DateTime(new Date(from(loanJson).getLong("[0].termDate")));

        assertThat(loanId, not(nullValue()));
        assertThat(initialInterest, not(nullValue()));
        assertThat(initialEndDate, not(nullValue()));
    }

    @When("^user extends the loan$")
    public void user_extends_loan() {
        given()
            .pathParam("loanId", loanId)
        .when()
            .post("/rest/loans/{loanId}/extend")
        .then()
            .assertThat().statusCode(200);
    }

    @Then("^user history has a loan with an extension registered$")
    public void user_history_has_extended_loan() {
        when()
            .get("/rest/loans")
        .then()
            .body("[0].id", equalTo(Integer.valueOf(loanId)));
    }

    @And("^the loan has it's interest increased by a factor of (.+)$")
    public void extended_loan_has_interest_increased(BigDecimal factor) {
        final BigDecimal extendedInterst = initialInterest.multiply(factor);
        when()
            .get("/rest/loans")
        .then()
            .body("[0].interest", equalTo(extendedInterst.floatValue()));
    }

    @And("^the end date is increased by (\\d+) week$")
    public void extended_loan_end_date_increased_one_week(Integer weekIncrease) {
        final DateTime increasedEndDate = this.initialEndDate.plusWeeks(weekIncrease);

        String dateString = get("/rest/loans").path("[0].termDate").toString();
        DateTime date = TestHelper.extractDate(dateString);

        assertThat(date.toDate(), within(5, TimeUnit.SECONDS, increasedEndDate.toDate()));
    }
}