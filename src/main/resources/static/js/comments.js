$(function () {
  var sockJs = new SockJS('/stomp');
  var stomp = Stomp.over(sockJs);

  window.subscribeComments = function subscribeComments($post) {
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
        });

    return false;
  });

  $(document).on("click", ".btn-comment", function () {
    var $btn = $(this);
    var $form = $btn.closest(".media-comment").find(".form-comment");
    $btn.slideUp();
    $form.removeClass("hidden").hide().slideDown();
  });
});