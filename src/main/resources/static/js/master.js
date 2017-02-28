$(function () {
  var csrfToken = $("meta[name='_csrf']").attr("content");
  var csrfHeader = $("meta[name='_csrf_header']").attr("content");
  var currentUserId = $("meta[name='_current_user_id']").attr("content");

  $(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(csrfHeader, csrfToken);
  });

  $(".btn-friend-add").click(function () {
    var userId = $(this).attr("data-user-id");
    $.ajax({
      url: "/users/" + userId + "/friendrequests",
      method: "POST"
    });
  });

  $(".btn-friend-accept").click(function () {
    var friendRequestId = $(this).attr("data-friend-request-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friends/" + friendRequestId,
      method: "PUT"
    });
  });

  $(".btn-friend-reject").click(function () {
    var friendRequestId = $(this).attr("data-friend-request-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friendrequests/" + friendRequestId,
      method: "DELETE"
    });
  });
});

