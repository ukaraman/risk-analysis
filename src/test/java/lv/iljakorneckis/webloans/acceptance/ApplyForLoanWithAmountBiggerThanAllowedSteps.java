package lv.iljakorneckis.webloans.acceptance;


import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lv.iljakorneckis.webloans.enums.RiskStatus;
import lv.iljakorneckis.webloans.exceptions.RiskAssessmentException;

import java.math.BigDecimal;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

public class ApplyForLoanWithAmountBiggerThanAllowedSteps {

    private BigDecimal amount;
    private Integer term;

    private Response response;

    @Given("^user wants a loan of EUR (\\d+) for (\\d+) days that exceeds maximum$")
    public void user_wants_a_loan_exceeding_maximum(BigDecimal amount, Integer term) {
        this.amount = amount;
        this.term = term;
    }

    @When("^user applies for a risky loan$")
    public void user_applies_for_a_too_risky_loan() {
        this.response =
        given()
            .param("amount", amount)
            .param("term", term)
        .when()
             .post("/rest/loans")
        .then()
            .extract()
                .response();
    }

    @Then("^response should contain an error code and a message$")
    public void response_should_contain_error_code_and_message() {
        String exceptionClass = response.getBody().jsonPath().getString("exception");
        String message = response.getBody().jsonPath().getString("message");

        assertThat(exceptionClass, equalTo(RiskAssessmentException.class.getCanonicalName()));

        assertThat(message, not(nullValue()));
        assertThat(message, containsString(RiskStatus.MAX_AMOUNT_EXCEEDED.name()));
    }

    @And("^response HTTP code should be (\\d+)$")
    public void response_code_should_be(Integer expectedResponseCode) {
        Integer receivedCode = response.getStatusCode();

        assertThat(receivedCode, equalTo(expectedResponseCode));
    }

    @And("^no loan should be created in the DB$")
    public void no_new_loans_should_be_created() {
        get("/rest/loans").then().assertThat().body("", empty());
    }
}
