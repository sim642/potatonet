Feature: User can comment post and see comments

  Scenario: I see my own added comments
    Given I am authenticated
    Given I have a friend
    Given my friend has a post with content "I look good"
    Given I have a comment "LOL! Who are you kidding?" for post with content "I look good"
    When I navigate to url "https://localhost:8443/"
    Then page contains a comment with text "LOL! Who are you kidding?"

  Scenario: I see friends added comments
    Given I am authenticated
    Given I have a friend
    Given my friend has a post with content "I look good"
    Given my friend has a comment "LOL! Who are you kidding?" for post with content "I look good"
    When I navigate to url "https://localhost:8443/"
    Then page contains a comment with text "LOL! Who are you kidding?"