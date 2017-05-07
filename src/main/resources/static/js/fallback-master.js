fallback.load({
  jQuery: [
    'https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js',
    '/js/lib/jquery-1.12.4.min.js'
  ],
  'jQuery.fn.modal': [
    'https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js',
    '/js/lib/bootstrap.min.js'
  ],
  Stomp: [
    'https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js',
    '/js/lib/stomp.min.js'
  ],
  SockJS: [
    'https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.2/sockjs.min.js',
    '/js/lib/sockjs.min.js'
  ],
  masterjs: [
    '/js/master.js'
  ]
}, {
  shim: {
    'jQuery.fn.modal': ['jQuery'],
    'masterjs': ['jQuery', 'jQuery.fn.modal', 'Stomp', 'SockJS']
  }
});