require('./db/database');

var express = require('express');
var app = express();
var server = require('http').createServer(app);
//var socket = require('socket.io').listen(server);

app.listen('3000', () => {
    console.log('Server started on port 3000...')
});

app.get('/', (req, res) => {
    console.log("Responde to root")
    res.send('server is running')
});
