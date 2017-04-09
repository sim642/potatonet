Feature: Change password and language settings

  Background: User logs in with his original password
    Given I am anonymous
    Given exists user with email "settings_test@eesti.ee" and pass "password_old" and eid_code "39101010101"
    When I navigate to url "https://localhost:8443/login"
    When I log in with email "settings_test@eesti.ee" and pass "password_old"

  Scenario: Original password stops working after password change
    When I navigate to url "https://localhost:8443/settings"
    When I change password to "password_new"
    When I navigate to url "https://localhost:8443/logout"
    When I log in with email "settings_test@eesti.ee" and pass "password_old"
    Then I am redirected to "https://localhost:8443/login?error"

  Scenario: New password works after password change
    When I navigate to url "https://localhost:8443/settings"
    When I change password to "password_new"
    When I navigate to url "https://localhost:8443/logout"
    When I log in with email "settings_test@eesti.ee" and pass "password_new"
    Then I am redirected to "https://localhost:8443/"

  Scenario: Password change fails when passwords aren't identical
    When I navigate to url "https://localhost:8443/settings"
    When I miswrite password confirmation
    Then page contains a div with class "alert-danger" which contains span with text "Passwords must match!"

  Scenario: Password must be at least 8 characters
    When I navigate to url "https://localhost:8443/settings"
    When I change password to "pass"
    Then page contains a div with class "alert-danger" which contains span with text "Password must be at least 8 characters long!"

  Scenario: Language can be changed
    When I navigate to url "https://localhost:8443/settings"
    When I switch language to "EE"
    When I navigate to my user view
    Then page contains a div with class "panel-user-details" which contains paragraph with id "gender" and text "Mees"
    When I navigate to url "https://localhost:8443/settings"
    When I switch language to "EN"
    When I navigate to my user view
    Then page contains a div with class "panel-user-details" which contains paragraph with id "gender" and text "Male"
