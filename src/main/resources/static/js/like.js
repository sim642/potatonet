$(function () {

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

});