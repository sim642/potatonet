var stomp = null;

function onPosts (msg) {
  $("#posts").prepend(msg.body);
}

$(function () {
  var sockJs = new SockJS('/stomp');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
    stomp.subscribe('/topic/posts/' + userId, onPosts);
  });

  var canLoad = true;
  var $window = $(window);
  $window.scroll(function () {
    if (canLoad && ($(document).height() - $window.height() == $window.scrollTop())) {
      canLoad = false;

      var lastPostId = $("#posts .panel-post").last().attr("data-post-id");

      $.get("/users/" + userId + "/posts", {
        beforePostId: lastPostId
      }, function (data) {
        var $data = $(data);
        $("#posts").append($data);
        canLoad = $data.length > 0;
      });
    }
  });
});

