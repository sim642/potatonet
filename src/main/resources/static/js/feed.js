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
    (callback || function () {})();
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
  var $post = $("#post");
  var $content = $("#content");
  var $postButton = $("#postButton");
  var $postLoader = $("#loader-post");

  $content.inputButton($postButton);

  stompConnect(function () {
    trySendStoredPost();

    var sendPost = function () {
      trySendStoredPost(function () {
        $.post("/", $post.serialize())
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
              $content.prop("readonly", false);
              $postButton.attr('disabled', true);
              $postLoader.hide();
            });
      });
    };

    $post.submit(function (event) {
      if ($content.val().trim().length !== 0) {
        $content.prop("readonly", true);
        $postButton.prop("disabled", true);
        $postLoader.show();

        if ("geolocation" in navigator) {
          var geolocationTimeout = 5000;

          var timeout = setTimeout(function () {
            console.warn("Geolocation timed out");
            sendPost();
          }, geolocationTimeout);

          navigator.geolocation.getCurrentPosition(function (position) {
            clearTimeout(timeout);
            $("#longitude").val(position.coords.longitude);
            $("#latitude").val(position.coords.latitude);
            sendPost();
          }, function error(err) {
            // Most likely geolocation being disabled by user.
            clearTimeout(timeout);
            console.warn(err);
            sendPost();
          }, {
            timeout: geolocationTimeout / 2
          });
        } else {
          sendPost();
        }
      }
      return false;
    });
  });
});