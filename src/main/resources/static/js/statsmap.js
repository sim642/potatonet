function initMap() {
  var mapCenterLocation;
  if (coords.length == 0 || coords[0].latitude == null) {
    // default center locations is Tartu(58.3776 N, 26.7290 E)
	mapCenterLocation = {'latitude' : 58.3776, 'longitude' : 26.7290};
  } else {
    mapCenterLocation = coords[0];
  }

  var map = new google.maps.Map(document.getElementById('map'), {
	zoom: 3,
	center: {lat: mapCenterLocation.latitude, lng: mapCenterLocation.longitude}
  });

  var markers = coords.map(function (location) {
	return new google.maps.Marker({
	  position: new google.maps.LatLng(location.latitude, location.longitude)
	});
  });

  var markerCluster = new MarkerClusterer(map, markers,
	  {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
}