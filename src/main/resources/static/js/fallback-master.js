fallback.load({
  jQuery: [
    'https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js',
    '/js/jquery-1.12.4.min.js'
  ],
  'jQuery.fn.modal': [
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js',
    '/js/bootstrap.min.js'
  ],
  masterjs: [
    '/js/master.js'
  ]
}, {
  shim: {
    'jQuery.fn.modal': ['jQuery'],
    'masterjs': ['jQuery', 'jQuery.fn.modal']
  }
});