var stomp = null;

function onFeed (msg) {
  $("#feed").prepend(msg.body);
}

$(function () {
  var sockJs = new SockJS('/feed-ws');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
    stomp.subscribe('/user/feed', onFeed);

    $("#post").submit(function (event) {
      var $content = $("#content");

      $.post("/post", $("#post").serialize());
      // stomp.send("/feed", {}, $content.val());
      $content.val("");

      return false;
    })
  });
});

