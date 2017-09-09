var http = require('http');
var url = require('url');
var factorial = require('./factorial');
var fibonacci = require('./fibonacci');

http.createServer(function (request, response) {
  var requestDetails = url.parse(request.url, true);

  if (requestDetails.pathname === '/factorial') {
    response.end(factorial(parseInt(requestDetails.query.param)).toString());
  }
  else if (requestDetails.pathname === '/fibonacci') {
    response.end(fibonacci(parseInt(requestDetails.query.param)).toString());
  }
  else if (requestDetails.pathname === '/id') {
    response.end("NodeJS");
  }
  else {
    response.end("OK");
  }
}).listen(1337, '0.0.0.0');

console.log('Server running');
