Feature: User is redirected to login page after logout

  Scenario: User is redirected to login page after logout
    Given I am authenticated
    When I navigate to url "https://localhost:8443/logout"
    Then I am redirected to "https://localhost:8443/login"