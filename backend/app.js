
/**
*	Load http module.
*/
var http = require("http");

/**
*	Create server instance and then bind it at port 8081
*/
http.createServer(function (request, response) {

   // Send the HTTP header 
   // HTTP Status: 200 : OK
   // Content Type: text/plain
   response.writeHead(200, {'Content-Type': 'text/plain'});
   
   // Send the response body as "Hello World"
   response.end('Hello World. Restaurant Applicaition\n');
}).listen(8081);

// Console will print the message
console.log('Server running at http://localhost:8081/');