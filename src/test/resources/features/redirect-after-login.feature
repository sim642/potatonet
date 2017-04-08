Feature: Redirect the user to the correct page after he has logged in

  Background:
    Given exists user with name "user123" and pass "password123"

  Scenario:
    Given user is anonymous
    When user navigates to url "https://localhost:8443/users"
    Then user is redirected to "https://localhost:8443/login"
    When user logs in with name "user123" and pass "password123"
    Then user is redirected to "https://localhost:8443/users"
