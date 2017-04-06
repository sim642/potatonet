$(function () {
  var sockJs = new SockJS('/stomp');
  var stomp = Stomp.over(sockJs);

  function subscribeComments($post) {
    var postId = $post.attr("data-post-id");
    stomp.subscribe('/topic/comments/' + postId, function (msg) {
      var commentId = msg.body;
      $.get("/comments/" + commentId, function (data) {
        $post.find(".list-comments li:last").before($(data));
      })
    });
  }

  stomp.connect({}, function () {
    $(".panel-post").each(function () {
      subscribeComments($(this));
    });

    $("body").on("DOMNodeInserted", ".panel-post", function () {
      subscribeComments($(this));
    });
  });
});