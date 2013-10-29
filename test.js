var vertx = require('vertx');
var console = require('vertx/console');

vertx.createHttpServer().requestHandler(function(req) {
  var file = req.path() === '/' ? 'index.html' : req.path();
  req.response.sendFile('webroot/' + file);
}).listen(8001)
