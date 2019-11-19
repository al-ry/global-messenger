var express = require('express');
var app = express();
var server = require('http').createServer(app);
var socket = require('socket.io').listen(server);


/*app.get('/', function(request, response) {
    respons.sendFile(__dirname + Name of app)
})*/

app.listen('3000', () => {
    console.log('Server started on port 3000')
})
