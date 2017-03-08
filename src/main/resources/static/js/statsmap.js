function initMap() {
  var map = new google.maps.Map(document.getElementById('map'), {
	zoom: 3,
	center: {lat: -28.024, lng: 140.887}
  });

  var labels = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';
  var markers = locations.map(function (location, i) {
	return new google.maps.Marker({
	  position: location,
	  label: labels[i % labels.length]
	});
  });

  var markerCluster = new MarkerClusterer(map, markers,
	  {imagePath: 'https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m'});
}

var locations = [
  {lat: -31.563910, lng: 147.154312},
  {lat: -33.718234, lng: 150.363181},
  {lat: -33.727111, lng: 150.371124},
  {lat: -33.848588, lng: 151.209834},
  {lat: -33.851702, lng: 151.216968},
  {lat: -34.671264, lng: 150.863657},
  {lat: -35.304724, lng: 148.662905},
  {lat: -36.817685, lng: 175.699196},
  {lat: -36.828611, lng: 175.790222},
  {lat: -37.750000, lng: 145.116667},
  {lat: -37.759859, lng: 145.128708},
  {lat: -37.765015, lng: 145.133858},
  {lat: -37.770104, lng: 145.143299}
];
