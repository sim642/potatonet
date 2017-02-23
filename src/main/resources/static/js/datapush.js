var stomp = null;

function onFeed(msg) {
  $("#feed").prepend(msg.body);
}

$(function () {
  var sockJs = new SockJS('/feed-ws');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
    stomp.subscribe('/user/feed', onFeed);
  });
});

