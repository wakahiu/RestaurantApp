
/**
*   Load http module.
*/
var http = require("http");
var express = require('express');
var app = express();
var fs = require("fs");
var MongoClient = require('mongodb').MongoClient;
var assert = require('assert');
var ObjectId = require('mongodb').ObjectID;
var url = 'mongodb://localhost:27017/';

// RESTful API listUsers
app.get('/listUsers', function (req, res) {
   fs.readFile( __dirname + "/" + "users.json", 'utf8', function (err, data) {
       console.log( data );
       res.end( data );
   });
})

app.get('/about', function (req, res) {
   fs.readFile( __dirname + "/" + "package.json", 'utf8', function (err, data) {
       console.log( data );
       res.end( data );
   });
})

app.get('/', function (req, res) {
  res.send('Restaurant app is running correctly.');
  console.log('User is issuing GET request')
});

var server = app.listen(8081, function () {

  var host = server.address().address
  var port = server.address().port

  console.log("Example app listening at http://localhost:%s", port)

})

MongoClient.connect(url, function(err, db) {
  assert.equal(null, err);
  console.log("Connected correctly to server.");
  db.close();
});

var insertDocument = function(db, callback) {
   db.collection('restaurants').insertOne( {
      "address" : {
         "street" : "2 Avenue",
         "zipcode" : "10075",
         "building" : "1480",
         "coord" : [ -73.9557413, 40.7720266 ]
      },
      "borough" : "Manhattan",
      "cuisine" : "Italian",
      "grades" : [
         {
            "date" : new Date("2014-10-01T00:00:00Z"),
            "grade" : "A",
            "score" : 11
         },
         {
            "date" : new Date("2014-01-16T00:00:00Z"),
            "grade" : "B",
            "score" : 17
         }
      ],
      "name" : "Vella",
      "restaurant_id" : "41704620"
   }, function(err, result) {
    assert.equal(err, null);
    console.log("Inserted a document into the restaurants collection.");
    callback(result);
  });
};

var insertLogin = function ( db, callback ) {
    db.collection ( 'login' ).insertOne ( {
        "username": "admin",
        "password": "nopassword"
    }, function(err, result) {
    assert.equal(err, null);
    console.log("Inserted one login information into the login collection.");
    callback(result);
    });
}

MongoClient.connect(url + 'insertdoc', function(err, db) {
  assert.equal(null, err);
  // insertDocument(db, function() {
  //     db.close();
  // });
 insertLogin ( db, function () {
    db.close();
 });
});
