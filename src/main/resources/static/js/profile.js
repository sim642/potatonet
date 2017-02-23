var stomp = null;

function onPosts (msg) {
  $("#posts").prepend(msg.body);
}

$(function () {
  var sockJs = new SockJS('/feed-ws');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
    stomp.subscribe('/topic/posts/' + $("#userId").val(), onPosts);
  });
});

