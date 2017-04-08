Feature: Redirect anonymous users to login page

  Scenario Outline:
    Given user is anonymous
    When user navigates to url "<url>"
    Then user is redirected to "<redirect>"
    Examples:
      | url                               | redirect                     |
      | https://localhost:8443/           | https://localhost:8443/login |
      | https://localhost:8443/users      | https://localhost:8443/login |
      | https://localhost:8443/posts      | https://localhost:8443/login |
      | https://localhost:8443/settings   | https://localhost:8443/login |
      | https://localhost:8443/statistics | https://localhost:8443/login |
      | https://localhost:8446/           | https://localhost:8446/login |