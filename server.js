var express = require('express');
var app = express();
var server = require('http').createServer(app);
var socket = require('socker.io').listen(server);

server.listen(3000);

app.get('/', function(request, response) {
    respons.sendFile(__dirname + /*Name of app*/)
})

jkhjkhjhkjhkjhk