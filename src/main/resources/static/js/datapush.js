var stompClient = null;

function connect() {
    var socket = new SockJS('/feed-ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/feed/websocket', function (post) {
            showPost(JSON.parse(post.body).content);
        });
    });
}

function sendPost() {
    stompClient.send("/fromSockJSClient", {}, JSON.stringify({'name': 'thing'}));
}

function showPost(post) {
    $("#feed").prepend(post);
}

$(function () {
    $("#postButton").click(function() { sendPost(); });
    connect();
});

