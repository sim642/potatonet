$('#mapModal').on('show.bs.modal', function (event) {
  var button = $(event.relatedTarget);
  var latitude = button.data('latitude');
  var longitude = button.data('longitude');
  var img = document.createElement("img");
  img.src = 'https://maps.googleapis.com/maps/api/staticmap?center=' + latitude + ',' + longitude + '&zoom=13&size=500x500&markers=' +
      + latitude + ',' + longitude + '&key=AIzaSyAaz-gJiZHQJRyTQXIAE9Nspc8dImoMEgA';
  img.classList.add('img-rounded');
  var $mapImage = $('#mapImage');
  $mapImage.empty();
  $mapImage.append(img);
});