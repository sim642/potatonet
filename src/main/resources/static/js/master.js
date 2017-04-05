var csrfToken = null;
var csrfHeader = null;
var currentUserId = null;

$(function () {
  csrfToken = $("meta[name='_csrf']").attr("content");
  csrfHeader = $("meta[name='_csrf_header']").attr("content");
  currentUserId = $("meta[name='_current_user_id']").attr("content");

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

  $(document).on("submit", ".banklinks", function (event, submit) {
    if (!submit) {
      var $this = $(this);
      var banklinkName = $("button[name=banklink][clicked]").val();

      $.get($this.attr("action"), {
        banklinkName: banklinkName
      }, function (data) {
        $.each(data.params, function (name, value) {
          var $input = $("<input></input>").attr("type", "hidden").attr("name", name).attr("value", value);
          $this.append($input);
        });

        $this.attr("action", data.url);
        // $this.submit(true);
        $this.trigger("submit", true);
      });

      return false;
    }
  });

  // http://stackoverflow.com/a/2066355
  $(document).on("click", "form input[type=submit], form button[type=submit]", function() {
    $("input[type=submit], button[type=submit]", $(this).parents("form")).removeAttr("clicked");
    $(this).attr("clicked", "clicked");
  });
});