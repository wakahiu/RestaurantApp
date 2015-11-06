
/**
*	Load http module.
*/
var http = require("http");
var express = require('express');
var app = express();
var fs = require("fs");

// RESTful API listUsers
app.get('/listUsers', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
       console.log( data );
       res.end( data );
   });
})

app.get('/', function (req, res) {
  res.send('Restaurant app is running correctly.');
});

var server = app.listen(8081, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("Example app listening at http://localhost:%s", port)

})