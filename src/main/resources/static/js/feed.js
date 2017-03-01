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
    })
  });
});

