var stomp = null;

function onFeed(msg) {
  var postId = msg.body;
  $.get("/posts/" + postId, function (data) {
    var $post = $(data);
    $("#feed").prepend($post);
    subscribeComments($post);
  });
}

function showStoredPostAlert() {
  var userStorage = JSON.parse(localStorage.getItem(currentUserId)) || {};
  var posts = (userStorage.posts || []);

  $("#stored-count").text(posts.length);
  if (posts.length > 0) {
    $("#alert-stored").removeClass("hidden");
  }
  else {
    $("#alert-stored").addClass("hidden");
  }
}

function storePost() {
  var userStorage = JSON.parse(localStorage.getItem(currentUserId)) || {};
  var posts = (userStorage.posts || []);
  posts.push($("#post").serialize());
  userStorage.posts = posts;
  localStorage.setItem(currentUserId, JSON.stringify(userStorage));
  showStoredPostAlert();
}

var trySendTimeout = null;

function trySendStoredPost(callback) {
  clearTimeout(trySendTimeout);

  var endCallback = function () {
    (callback || function(){})();
    trySendTimeout = setTimeout(trySendStoredPost, 60 * 1000);
  };

  var userStorage = JSON.parse(localStorage.getItem(currentUserId)) || {};
  var posts = (userStorage.posts || []);
  if (posts.length > 0) {
    var post = posts.shift();
    $.post("/", post)
        .done(function () {
          userStorage.posts = posts;
          localStorage.setItem(currentUserId, JSON.stringify(userStorage));
          showStoredPostAlert();
          trySendStoredPost();
        })
        .fail(endCallback);
  }
  else {
    endCallback();
  }
}

$(function () {
  var sockJs = new SockJS('/stomp');
  stomp = Stomp.over(sockJs);
  stomp.connect({}, function () {
	$.each(userIds, function (i, userId) {
	  stomp.subscribe('/topic/posts/' + userId, onFeed);
	});

    trySendStoredPost();

	var sendPost = function() {
	  var $content = $("#content");
	  trySendStoredPost(function () {
		$.post("/", $("#post").serialize())
			.fail(function (jqXHR) {
			  // http://stackoverflow.com/a/28404728
        switch (jqXHR.readyState) {
          case 0:
            storePost();
            break;

          case 4:
            alert("HTTP error (" + jqXHR.status + ")");
            break;

          default:
            alert("Unknown error");
            break;
        }
      })
			.always(function () {
			  $content.val("");
			  $('#postButton').attr('disabled', true);
			});
	  });
	};

	$("#post").submit(function (event) {
	  if ($("#content").val().trim().length != 0) {
      if ("geolocation" in navigator) {
        navigator.geolocation.getCurrentPosition(function (position) {
          $("#longitude").val(position.coords.longitude);
          $("#latitude").val(position.coords.latitude);
          sendPost();
        }, function error(err) {
          // Most likely geolocation being disabled by user.
          sendPost();
          console.warn(err);
        }, {
          timeout: 5000
        });
      } else {
        sendPost();
      }
    }
	  return false;
	});
  });

  var canLoad = true;
  var $window = $(window);
  var $loader = $("#loader");

  $window.scroll(function () {
	if (canLoad && ($(document).height() - $window.height() - $window.scrollTop() < 100)) {
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

$(document).ready(function(){
    $('#postButton').attr('disabled', true);

    $('#content').keypress(function (event) {
      if (event.keyCode == 13 && !event.shiftKey) {
        $('#post').submit();
        event.preventDefault();
        return false;
      }
    });

    $('#content').keyup(function(){
        if($(this).val().trim().length !=0)
            $('#postButton').attr('disabled', false);
        else
            $('#postButton').attr('disabled', true);
    });
});