function initMap() {
  if (coords == null || coords.length == 0 || coords[0] == null) {
	var mapContainer = document.getElementById('mapContainer');
	mapContainer.parentNode.removeChild(mapContainer);
   	return;
  }

  var map = new google.maps.Map(document.getElementById('map'), {
	zoom: 11,
	center: {lat: coords[0].latitude, lng: coords[0].longitude}
  });

  var markers = coords.map(function (location) {
	return new google.maps.Marker({
	  position: new google.maps.LatLng(location.latitude, location.longitude)
	});
  });

  var markerCluster = new MarkerClusterer(map, markers,
	  {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
}