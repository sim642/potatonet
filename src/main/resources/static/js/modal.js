var mapModalPopping = false; // hack to allow modal's event handlers to know whether modal show/hide was triggered by history state pop

$('#mapModal').on('show.bs.modal', function (event) {
  var post = $(event.relatedTarget);
  var latitude = post.data('latitude');
  var longitude = post.data('longitude');
  if (latitude != -91 && longitude != -181) {
	initMap(longitude, latitude);
  } else {
    $('#map').text("No location information available for this post!");
  }

  if (!mapModalPopping) {
    var $post = post.closest(".panel-post");
    var postId = $post.attr("data-post-id");
    window.history.pushState({map: postId}, {}, "/posts/" + postId);
  }
  mapModalPopping = false;
});

$('#mapModal').on('shown.bs.modal', function () {
  google.maps.event.trigger(map, "resize");
});

$('#mapModal').on('hide.bs.modal', function () {
  if (!mapModalPopping) {
    window.history.back();
  }
  mapModalPopping = false;
});

window.onpopstate = function (event) {
  mapModalPopping = true;
  if (event.state !== null && ((postId = event.state.map) !== null)) {
    var $post = $(".panel-post[data-post-id='" + postId + "']");
    $('#mapModal').modal('show', $post.find(".btn-modal-map"));
  }
  else {
    $('#mapModal').modal('hide');
  }
};

function initMap(longitude, latitude) {
  var postLocation = {lat: latitude, lng: longitude};
  var map = new google.maps.Map(document.getElementById('map'), {
	zoom: 10,
	center: postLocation
  });
  var marker = new google.maps.Marker({
	position: postLocation,
	map: map
  });
}

