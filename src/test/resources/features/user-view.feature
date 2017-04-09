Feature: I can see other users gender and birthday in the user view

  Background:
    Given I am authenticated

  Scenario Outline: I see users gender
    Given exists user with email "<email>" and eid_code "<eid_code>"
    When I navigate to users "<email>" view
    Then page contains a div with class "panel-user-details" which contains paragraph with id "gender" and text "<gender>"
    Examples:
      | email          | eid_code    | gender |
      | male@test.ee   | 39502072534 | Male   |
      | female@test.ee | 49502072534 | Female |

  Scenario Outline: I see users birthdate
    Given exists user with email "<email>" and eid_code "<eid_code>"
    When I navigate to users "<email>" view
    Then page contains a div with class "panel-user-details" which contains paragraph with id "birthday" and text "<birthday>"
    Examples:
      | email      | eid_code    | birthday   |
      | a0@test.ee | 10101010101 | 1801-01-01 |
      | a1@test.ee | 39502072534 | 1995-02-07 |
      | a2@test.ee | 40812312534 | 1908-12-31 |
      | a3@test.ee | 50110212534 | 2001-10-21 |
      | a4@test.ee | 81911302534 | 2119-11-30 |