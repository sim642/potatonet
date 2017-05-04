var masterjs = true; // hax

window.location.pathbase = window.location.pathname !== "/" ? window.location.pathname : "";

var csrfToken = null;
var csrfHeader = null;
var currentUserId = null;
var stomp = null;

function setupCsrf() {
  csrfToken = $("meta[name='_csrf']").attr("content");
  csrfHeader = $("meta[name='_csrf_header']").attr("content");

  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(csrfHeader, csrfToken);
  });
}

function setupCurrentUser() {
  currentUserId = $("meta[name='_current_user_id']").attr("content");
}

function setupFriendButtons() {
  $(document).on("click", ".btn-friend-add", function () {
    var $btns = $(this).closest(".btns-friend");
    var userId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + userId + "/friendrequests",
      method: "POST"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });

  $(document).on("click", ".btn-friend-accept", function () {
    var $btns = $(this).closest(".btns-friend");
    var friendRequestId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friends/" + friendRequestId,
      method: "PUT"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });

  $(document).on("click", ".btn-friend-reject", function () {
    var $btns = $(this).closest(".btns-friend");
    var friendRequestId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friendrequests/" + friendRequestId,
      method: "DELETE"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });

  $(document).on("click", ".btn-friend-remove", function () {
    var $btns = $(this).closest(".btns-friend");
    var friendId = $btns.attr("data-user-id");
    $.ajax({
      url: "/users/" + currentUserId + "/friends/" + friendId,
      method: "DELETE"
    }).done(function (data) {
      $btns.replaceWith($(data));
    });

    return false;
  });
}

function setupBanklinks() {
  $(document).on("submit", ".banklinks", function (event, submit) {
    if (!submit) {
      var $this = $(this);
      var banklinkName = $("button[name=banklink][clicked]").val();

      $.get($this.attr("action"), {
        banklinkName: banklinkName
      }, function (data) {
        $.each(data.params, function (name, value) {
          var $input = $("<input></input>").attr("type", "hidden").attr("name", name).attr("value", value);
          $this.append($input);
        });

        $this.attr("action", data.url);
        // $this.submit(true);
        $this.trigger("submit", true);
      });

      return false;
    }
  });

  // http://stackoverflow.com/a/2066355
  $(document).on("click", "form input[type=submit], form button[type=submit]", function () {
    $("input[type=submit], button[type=submit]", $(this).parents("form")).removeAttr("clicked");
    $(this).attr("clicked", "clicked");
  });
}

function setupHash() {
  // https://github.com/twbs/bootstrap/issues/1768#issuecomment-6531466
  var shiftWindow = function () {
    scrollBy(0, -70);
  };

  if (location.hash)
    shiftWindow();
  window.addEventListener("hashchange", shiftWindow);
}

function setupLikeButtons() {
  window.subscribeLikes = function subscribeLikes($post) {
    var postId = $post.attr("data-post-id");
    stomp.subscribe('/topic/likes/' + postId, function (msg) {
      // find here because the like button gets replaced
      var $btn = $post.find(".like-btn");
      var $label = $btn.find(".label");

      var likeCount = msg.body;
      $label.text(likeCount);
    });
  };

  stompConnect(function () {
    $(".panel-post").each(function () {
      subscribeLikes($(this));
    });
  });


  $(document).on("submit", ".like", function (event) {
    var panelpost = $(event.target).closest(".panel-post");
    var post_id = panelpost.attr("data-post-id");
    var button = panelpost.find(".like-btn");

    $.ajax({
      url: '/like/' + post_id,
      type: 'PUT'
    }).done(function (data) {
      button.replaceWith($(data));
    });

    return false;
  });
}


var stompConnects = [];

function stompConnect(callback) {
  if (stomp.connected) {
    callback();
  }
  else {
    stompConnects.push(callback);
    if (stompConnects.length == 1) {
      stomp.connect({}, function () {
        $.each(stompConnects, function (i, callback) {
          callback();
        });
      });
    }
  }
}

function setupWebsocket() {
  var sockJs = new SockJS('/stomp');
  stomp = Stomp.over(sockJs);
}

function setupComments() {
  window.subscribeComments = function subscribeComments($post) {
    var $formComment = $post.find(".form-comment");
    var $btnComment = $formComment.find(".btn-comment");
    var $textarea = $formComment.find("textarea");

    $textarea.inputButton($btnComment);

    var postId = $post.attr("data-post-id");
    stomp.subscribe('/topic/comments/' + postId, function (msg) {
      var commentId = msg.body;
      $.get("/comments/" + commentId, function (data) {
        $post.find(".list-comments li:last").before($(data));
      })
    });
  };

  stompConnect(function () {
    $(".panel-post").each(function () {
      subscribeComments($(this));
    });
  });

  $(document).on("submit", ".form-comment", function (event) {
    var $form = $(this);
    var $post = $form.closest(".panel-post");
    var postId = $post.attr("data-post-id");
    var $content = $form.find("textarea");
    if ($content.val().trim().length !== 0) {
      $.post("/posts/" + postId + "/comments", $form.serialize())
          .fail(function (jqXHR) {
            // http://stackoverflow.com/a/28404728
            switch (jqXHR.readyState) {
              case 0:
                alert("Network error");
                break;

              case 4:
                alert("HTTP error (" + jqXHR.status + ")");
                break;

              default:
                alert("Unknown error");
                break;
            }
          })
          .done(function () {
            $content.val("");
            $form.find(".btn-comment").attr("disabled", true);
          });
    }

    return false;
  });

  $(document).on("click", ".btn-comment-expand", function () {
    var $btn = $(this);
    var $form = $btn.closest(".media-comment").find(".form-comment");
    $btn.slideUp();
    $form.removeClass("hidden").hide().slideDown();
  });
}

function setupFeed() {
  var $feed = $("#feed");
  if (!$feed.exists())
    return;

  var feedUserIds = $feed.attr("data-feed-user-ids").split(",");

  function onFeed(msg) {
    var postId = msg.body;
    $.get("/posts/" + postId, function (data) {
      var $post = $(data);
      $feed.prepend($post);
      subscribeComments($post);
      subscribeLikes($post);
    });
  }

  stompConnect(function () {
    $.each(feedUserIds, function (i, userId) {
      stomp.subscribe('/topic/posts/' + userId, onFeed);
    });
  });

  var canLoad = true;
  var $window = $(window);
  var $loader = $("#loader");

  $window.scroll(function () {
    if (canLoad && ($(document).height() - $window.height() - $window.scrollTop() < 100)) {
      canLoad = false;

      var lastPostId = $feed.find(".panel-post").last().attr("data-post-id");
      $loader.show();

      $.get(window.location.pathbase + "/posts", {
        beforePostId: lastPostId
      }, function (data) {
        var $data = $(data);
        $loader.hide();
        $feed.append($data);
        canLoad = $data.length > 0;
      });
    }
  });
}

// http://stackoverflow.com/a/31047
jQuery.fn.exists = function () {
  return this.length > 0;
};

$.fn.inputButton = function ($btn) {
  var $input = this;
  var $form = $btn.closest("form");

  $input.keypress(function (event) {
    if (event.keyCode === 13 && !event.shiftKey) {
      $form.submit();
      event.preventDefault();
      return false;
    }
  });

  $btn.attr("disabled", true);
  $input.keyup(function (event) {
    $btn.attr("disabled", $(this).val().trim().length === 0);
  });

  return this;
};

function setupPopover() {
  $('[data-toggle="popover"]').popover();
}

$(function () {
  setupCsrf();
  setupCurrentUser();
  setupFriendButtons();
  setupBanklinks();
  setupHash();
  setupWebsocket();
  setupLikeButtons();
  setupComments();
  setupFeed();
  setupPopover();
});