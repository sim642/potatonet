Feature: Posts can be liked

  Scenario: I can like my own posts
    Given I am authenticated
    When I navigate to url "https://localhost:8443/"
    When I create a post with content "my_post"
    Then page contains a post with text "my_post" and 0 likes
    When I like a post with content "my_post"
    Then page contains a post with text "my_post" and 1 likes

  Scenario: I see a friend liked post on my feed
    Given I am authenticated
    Given exists user with email "like_test@eesti.ee"
    Given I am friends with user "like_test@eesti.ee"
    Given user "like_test@eesti.ee" has a post with content "Jar Jar Binks is my favourite character"
    When user "like_test@eesti.ee" likes a post with content "Jar Jar Binks is my favourite character"
    When I navigate to url "https://localhost:8443/"
    Then page contains a post with text "Jar Jar Binks is my favourite character" and 1 likes
    When I like a post with content "Jar Jar Binks is my favourite character"
    Then page contains a post with text "Jar Jar Binks is my favourite character" and 2 likes
