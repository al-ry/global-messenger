const io = require('./server').io

var connectedUsers = { };

module.exports = function(socket) {
    console.log('User connected:' + socket)

    socket.on()
}