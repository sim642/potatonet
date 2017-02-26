fallback.load({
  jQuery: [
    'https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js',
    '/js/jquery-1.12.4.min.js'
  ],
  'jQuery.fn.modal': [
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js',
    '/js/bootstrap.min.js'
  ],
  bootstrapCSS: [
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css',
    '/css/bootstrap.min.css'
  ],
  bootstrapThemeCSS: [
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css',
    '/css/bootstrap-theme.min.css'
  ]
}, {
  shim: {
    'jQuery.fn.modal': ['jQuery']
  }
});