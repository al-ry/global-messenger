const io = require('./server').io
var sessionDB = require('./db/sessions')

var Message = require('./models/message')
var connectedUsers = [];
var usersCookies = [];

module.exports = function(socket) {
    console.log('User connected:' + socket.id)

    // connectedUsers.push(socket.id)
    // console.log(connectedUsers)

    socket.on('user_connected', (userPhone, cookie) => {
        if (usersCookies[userPhone] != undefined)
        {
            var sessionId = GetSessionId(cookie)
            sessionDB.get(sessionId, (error, session) => { 
                console.log(session)
            })
            sessionDB.destroy(sessionId, (err) => {
                if (err) throw err;
            })
        }
        usersCookies[userPhone] = cookie
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
    socket.on('disconnection', (userPhone) => {
        delete connectedUsers[userPhone]       
        console.log(connectedUsers)
        console.log('User disconnected ' + socket.id)
    })
}

function isUser(userList, userPhone) {
    return userPhone in userList;
}


function GetSessionId(cookie) {
    var endPos = cookie.indexOf('.')
    var cookieId = cookie.slice(2, endPos)
    return cookieId;
}