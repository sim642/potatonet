Feature: See only your friends in Friends tab
  
  Scenario: Go to Friends tab, see only friends
    Given exists user with email "friend1"
    Given exists user with email "friend2"
    Given exists user with email "friend3"
    Given exists user with email "friend4"
    Given I am authenticated
    Given I am friends with user "friend1"
    Given I am friends with user "friend3"
    Given I am not friends with user "friend2"
    Given I am not friends with user "friend4"
    When I navigate to my friends view
    Then page contains a user panel with name "friend1"
    Then page contains a user panel with name "friend3"
    Then page doesn't contain a user panel with name "friend2"
    Then page doesn't contain a user panel with name "friend4"
