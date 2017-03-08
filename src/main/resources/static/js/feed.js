var stomp = null;

function onFeed (msg) {
  $("#feed").prepend(msg.body);
}

$(function () {
  var sockJs = new SockJS('/stomp');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
    $.each(userIds, function (i, userId) {
      stomp.subscribe('/topic/posts/' + userId, onFeed);
    });

    $("#post").submit(function (event) {
      var $content = $("#content");

      $.post("/", $("#post").serialize());
      $content.val("");

      return false;
    });
  });

  var canLoad = true;
  var $window = $(window);
  var $loader = $("#loader");
  $loader.hide();

  $window.scroll(function () {
    if (canLoad && ($(document).height() - $window.height() == $window.scrollTop())) {
      canLoad = false;

      var lastPostId = $("#feed .panel-post").last().attr("data-post-id");
      $loader.show();

      $.get("/posts", {
        beforePostId: lastPostId
      }, function (data) {
        var $data = $(data);
        $loader.hide();
        $("#feed").append($data);
        canLoad = $data.length > 0;
      });
    }
  });
});

