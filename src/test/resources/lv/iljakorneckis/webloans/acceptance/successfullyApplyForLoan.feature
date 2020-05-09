Feature: Successful loan application
  Scenario: User applies for a loan, providing valid data.
    Given user wants a loan of EUR 500 for 20 days
    When user applies for a loan with status 200
    Then user has a new loan in his loan history