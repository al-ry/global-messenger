const io = require('./server').io

var connectedUsers = [];

module.exports = function(socket) {
    console.log('User connected:' + socket.id)

    socket.on('user_connected', (userPhone) => {
        connectedUsers[userPhone] = socket.id
        console.log(connectedUsers)
    })

    socket.on('send_message', (data) => {
        //connectedUsers.find(data.receiver)
        var receiverSocketId = connectedUsers[data.receiver]
        io.to(receiverSocketId).emit("new_message", data.msg)
    })
    socket.on('disconnect', (userPhone) => {
        connectedUsers.slice(userPhone, 1)
        console.log(connectedUsers)
        console.log('User disconnected ' + socket.id)
    })
}

function isUser(userList, userPhone) {
    return userPhone in userList;
}