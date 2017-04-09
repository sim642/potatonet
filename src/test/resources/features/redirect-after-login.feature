Feature: Redirect the user to the correct page after he has logged in

  Background:
    Given exists user with email "user123" and pass "password123"

  Scenario Outline: User is redirected to the correct page after login
    Given I am anonymous
    When I navigate to url "<url>"
    Then I am redirected to "https://localhost:8443/login"
    When I log in with email "user123" and pass "password123"
    Then I am redirected to "<url>"
    Examples:
      | url                               |
      | https://localhost:8443/users      |
      | https://localhost:8443/statistics |
      | https://localhost:8443/donate     |
      | https://localhost:8443/settings   |