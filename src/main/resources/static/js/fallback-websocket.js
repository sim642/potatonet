fallback.load({
  Stomp: [
    'https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js',
    '/js/stomp.min.js'
  ],
  SockJS: [
    'https://cdnjs.cloudflare.com/ajax/libs/sockjs-client/1.1.2/sockjs.min.js',
    '/js/sockjs.min.js'
  ]
}, {
  shim: {
    'Stomp': ['jQuery'],
    'SockJS': ['jQuery', 'Stomp']
  }
});