$(function () {
  var csrfToken = $("meta[name='_csrf']").attr("content");
  var csrfHeader = $("meta[name='_csrf_header']").attr("content");
  var currentUserId = $("meta[name='_current_user_id']").attr("content");

  $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(csrfHeader, csrfToken);
  });

  $(document).on("click", ".btn-friend-add", function () {
    var $btns = $(this).closest(".btns-friend");
    var userId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + userId + "/friendrequests",
      method: "POST"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });

  $(document).on("click", ".btn-friend-accept", function () {
    var $btns = $(this).closest(".btns-friend");
    var friendRequestId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friends/" + friendRequestId,
      method: "PUT"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });

  $(document).on("click", ".btn-friend-reject", function () {
    var $btns = $(this).closest(".btns-friend");
    var friendRequestId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friendrequests/" + friendRequestId,
      method: "DELETE"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });

  $(document).on("click", ".btn-friend-remove", function () {
    var $btns = $(this).closest(".btns-friend");
    var friendId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friends/" + friendId,
      method: "DELETE"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });
});