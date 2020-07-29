const io = require('./server').io

var Message = require('./models/message')
var connectedUsers = [];

module.exports = function(socket) {
    console.log('User connected:' + socket.id)

    // connectedUsers.push(socket.id)
    // console.log(connectedUsers)

    socket.on('user_connected', (userPhone) => {
        // if (connectedUsers[userPhone] != undefined)
        // {
        //     io.emit('log_out')
        // }
        connectedUsers[userPhone.toString()] = socket.id
        console.log(connectedUsers)
    })


    socket.on('send_message', (data) => {
        if(connectedUsers[data.receiver] != undefined)
        {
            //newMessage = new Message(data.sender, data.receiver, data.msg, data.date)
            var receiverSocketId = connectedUsers[data.receiver]
            io.to(receiverSocketId).emit("new_message", data.msg)
        }
        //newMessage.Save()
        //add to db
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