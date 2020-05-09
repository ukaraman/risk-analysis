Feature: Receive risk assessment error
  @spring-start
  Scenario: User applies for a loan, providing amount that is larger than allowed.
    Given user wants a loan of EUR 1500 for 20 days that exceeds maximum
    When user applies for a risky loan
    Then response should contain an error code and a message
    And response HTTP code should be 422
    And no loan should be created in the DB