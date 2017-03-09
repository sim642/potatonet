$('#mapModal').on('show.bs.modal', function (event) {
  var post = $(event.relatedTarget);
  var latitude = post.data('latitude');
  var longitude = post.data('longitude');
  initMap(longitude, latitude);
});

$('#mapModal').on('shown.bs.modal', function () {
  google.maps.event.trigger(map, "resize");
});

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

