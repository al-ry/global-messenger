const io = require('./server').io
var sessionDB = require('./db/sessions')

var Message = require('./models/message');
var connectedUsers = [];
var usersCookies = [];

module.exports = function(socket) {

    socket.on('user_connected', (userPhone, cookie) => {
        if (usersCookies[userPhone] != undefined)
        {
            io.to(connectedUsers[userPhone]).emit('log_out')
            var sessionId = GetSessionId(usersCookies[userPhone])
            sessionDB.destroy(sessionId, (err) => {
                if (err) throw err;
            })
        }
        usersCookies[userPhone] = cookie
        connectedUsers[userPhone.toString()] = socket.id
    })

    socket.on('resume_session', (userPhone) => {
        connectedUsers[userPhone.toString()] = socket.id
    })

    socket.on('send_message', (sender, receiver, msg, date) => {
        var newMessage = new Message(sender, receiver, msg, date)
        if(connectedUsers[receiver] != undefined)
        {        
            var receiverSocketId = connectedUsers[receiver]
        
            io.to(receiverSocketId).emit("new_message", msg, date)
            io.to(receiverSocketId).emit('display_last_message', sender, msg, date)
        }
        newMessage.Save()
    })

    socket.on('is_typing', (isTyping, receiver) =>
    {
        var receiverSocketId = connectedUsers[receiver]
        io.to(receiverSocketId).emit("user_is_typing", isTyping)
    })

    socket.on('disconnection', (userPhone) => {
        delete connectedUsers[userPhone]   
        delete usersCookies[userPhone]
    })
}

function GetSessionId(cookie) {
    var endPos = cookie.indexOf('.')
    var cookieId = cookie.slice(2, endPos)
    return cookieId;
}