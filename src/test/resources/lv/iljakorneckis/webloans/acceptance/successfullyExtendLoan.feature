Feature: Successful loan extension
  @spring-end
  Scenario: User extends an existing loan one time
    Given user has a loan registered
    When user extends the loan
    Then user history has a loan with an extension registered
    And the loan has it's interest increased by a factor of 1.5
    And the end date is increased by 1 week