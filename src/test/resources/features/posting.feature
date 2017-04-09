Feature: Creating posts in the feed

  Scenario: Create a post
    Given I am authenticated
    When I navigate to url "https://localhost:8443/"
    When I create a post with content "This be my post!"
    When I navigate to my user view
    Then page contains a post with text "This be my post!"