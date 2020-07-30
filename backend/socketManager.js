const io = require('./server').io
var sessionDB = require('./db/sessions')

var Message = require('./models/message')
var connectedUsers = [];
var usersCookies = [];

module.exports = function(socket) {

    console.log('User connected:' + socket.id)

    //connectedUsers.push(socket.id)
    console.log(connectedUsers)

    socket.on('user_connected', (userPhone, cookie) => {
        console.log('=======user_connected=======')
        if (usersCookies[userPhone] != undefined)
        {
            console.log('=======user_connected if=======')
            io.to(connectedUsers[userPhone]).emit('log_out')
            var sessionId = GetSessionId(usersCookies[userPhone])
            console.log("logging out user " + userPhone + "with sessionId " + sessionId)
            sessionDB.destroy(sessionId)
            console.log('=======user_connected if=======')
        }

        usersCookies[userPhone] = cookie
        connectedUsers[userPhone.toString()] = socket.id
        console.log(connectedUsers)
        console.log(usersCookies)
        console.log('=======user_connected=======')
    })

    socket.on('send_message', (sender, receiver, msg, date) => {
        if(connectedUsers[receiver] != undefined)
        {
            var newMessage = new Message(sender, receiver, msg, date)
            var receiverSocketId = connectedUsers[receiver]
            io.to(receiverSocketId).emit("new_message", msg, date)
        }
        newMessage.Save()
        //add to db
    })
    socket.on('disconnection', (userPhone) => {
        console.log('=======disconnection=======')
        console.log(connectedUsers)
        console.log(connectedUsers[userPhone]) 
        console.log(usersCookies)
        console.log(usersCookies[userPhone]) 
        delete connectedUsers[userPhone]   
        delete usersCookies[userPhone]
        console.log(connectedUsers[userPhone]) 
        console.log(connectedUsers)
        console.log('User disconnected ' + socket.id + "," + userPhone)  
        console.log('=======disconnection=======') 
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