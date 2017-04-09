$(function () {
  var sockJs = new SockJS('/stomp');
  var stomp = Stomp.over(sockJs);

  window.subscribeComments = function subscribeComments($post) {
    var $formComment = $post.find(".form-comment");
    var $btnComment = $formComment.find(".btn-comment");
    var $textarea = $formComment.find("textarea");

    $textarea.keypress(function (event) {
      if (event.keyCode == 13 && !event.shiftKey) {
        $formComment.submit();
        event.preventDefault();
        return false;
      }
    });

    $btnComment.attr("disabled", true);
    $textarea.keyup(function (event) {
      $btnComment.attr("disabled", $(this).val().trim().length == 0);
    });

    var postId = $post.attr("data-post-id");
    stomp.subscribe('/topic/comments/' + postId, function (msg) {
      var commentId = msg.body;
      $.get("/comments/" + commentId, function (data) {
        $post.find(".list-comments li:last").before($(data));
      })
    });
  };

  stomp.connect({}, function () {
    $(".panel-post").each(function () {
      subscribeComments($(this));
    });
  });

  $(document).on("submit", ".form-comment", function (event) {
    var $form = $(this);
    var $post = $form.closest(".panel-post");
    var postId = $post.attr("data-post-id");
    var $content = $form.find("textarea");
    if ($content.val().trim().length != 0) {
      $.post("/posts/" + postId + "/comments", $form.serialize())
          .fail(function (jqXHR) {
            // http://stackoverflow.com/a/28404728
            switch (jqXHR.readyState) {
              case 0:
                alert("Network error");
                break;

              case 4:
                alert("HTTP error (" + jqXHR.status + ")");
                break;

              default:
                alert("Unknown error");
                break;
            }
          })
          .done(function () {
            $content.val("");
            $form.find(".btn-comment").attr("disabled", true);
          });
    }

    return false;
  });

  $(document).on("click", ".btn-comment-expand", function () {
    var $btn = $(this);
    var $form = $btn.closest(".media-comment").find(".form-comment");
    $btn.slideUp();
    $form.removeClass("hidden").hide().slideDown();
  });
});