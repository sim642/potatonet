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
});

