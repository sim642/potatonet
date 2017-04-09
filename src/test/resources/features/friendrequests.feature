Feature: It's possible to add and remove friend requests and confirm and delete friendships

  Background:
    Given I am authenticated
    Given exists user with email "user123" and pass "password123"
    Given I am not friends with user "user123"
    Given user "user123" has sent friend request to me
    When I navigate to url "https://localhost:8443/users"

  Scenario: I can see friend requests
    Then page contains a user panel with name "user123" with a button of class "btn-friend-accept"
    Then page contains a user panel with name "user123" with a button of class "btn-friend-reject"

  Scenario: I can accept friend requests
    When I click on user panel button with class "btn-friend-accept" for user "user123"
    Then page contains a user panel with name "user123" with a button of class "btn-friend-remove"

  Scenario: I can reject friend requests
    When I click on user panel button with class "btn-friend-reject" for user "user123"
    Then page contains a user panel with name "user123" with a button of class "btn-friend-add"

  Scenario: I can remove friends
    When I click on user panel button with class "btn-friend-accept" for user "user123"
    Then page contains a user panel with name "user123" with a button of class "btn-friend-remove"
    When I click on user panel button with class "btn-friend-remove" for user "user123"
    Then page contains a user panel with name "user123" with a button of class "btn-friend-add"
