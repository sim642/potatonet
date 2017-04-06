var stomp = null;

function onPosts (msg) {
  var postId = msg.body;
  $.get("/posts/" + postId, function (data) {
    var $post = $(data);
    $("#posts").prepend($post);
    subscribeComments($post);
  });
}

$(function () {
  var sockJs = new SockJS('/stomp');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
    $.each(userIds, function (i, userId) {
      stomp.subscribe('/topic/posts/' + userId, onPosts);
    });
  });

  var canLoad = true;
  var $window = $(window);
  var $loader = $("#loader");

  $window.scroll(function () {
    if (canLoad && ($(document).height() - $window.height() == $window.scrollTop())) {
      canLoad = false;

      var lastPostId = $("#posts .panel-post").last().attr("data-post-id");
      $loader.show();

      $.get("/users/" + userId + "/posts", {
        beforePostId: lastPostId
      }, function (data) {
        var $data = $(data);
        $loader.hide();
        $("#posts").append($data);
        canLoad = $data.length > 0;
      });
    }
  });
});

