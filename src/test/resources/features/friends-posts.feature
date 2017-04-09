Feature: Posts are visible on the user view, feed and as a standalone post

  Background:
    Given I am authenticated
    Given I have a friend
    Given my friend has a post with content "This is a test post"

  Scenario: Post is visible in the feed
    When I navigate to url "https://localhost:8443/"
    Then page contains a post with text "This is a test post"

  Scenario: Post is visible in my friends user view
    When I navigate to my friend user view
    Then page contains a post with text "This is a test post"