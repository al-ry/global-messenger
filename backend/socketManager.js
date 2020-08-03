const io = require('./server').io
var sessionDB = require('./db/sessions')

var Message = require('./models/message');
var connectedUsers = [];
var usersCookies = [];

module.exports = function(socket) {

<<<<<<< HEAD
    //console.log('User connected:' + socket.id)

    //connectedUsers.push(socket.id)
    //console.log(connectedUsers)

    socket.on('user_connected', (userPhone, cookie) => {
        console.log('=======user_connected=======')
        if (usersCookies[userPhone] != undefined)
        {
            console.log('=======user_connected if=======')
            console.log('Socket array start')
            console.log(connectedUsers)
            console.log('Socket array end') 
            console.log('Loggin out by socket: ' + connectedUsers[userPhone])
            io.to(connectedUsers[userPhone]).emit('log_out')
            var sessionId = GetSessionId(usersCookies[userPhone])
            console.log("logging out user " + userPhone + "with sessionId " + sessionId)
            sessionDB.destroy(sessionId, (err) => {
                if (err) throw err;
            })
            console.log('=======user_connected if=======')
        }
    

        usersCookies[userPhone] = cookie
        connectedUsers[userPhone.toString()] = socket.id
        console.log("After user conected array connectedUser:")
        console.log(connectedUsers)
        console.log(' And array usersCookies')
        console.log(usersCookies)
        console.log('=======user_connected=======')
    })

    socket.on('resume_session', (userPhone, cookie) => {
        console.log('=======resume_session=======')
        connectedUsers[userPhone.toString()] = socket.id
        console.log(connectedUsers)
        console.log(usersCookies)
        console.log('=======resume_session=======')
=======
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
>>>>>>> f998f98166c518ea2e075e66312dd2d1281299f0
    })

    socket.on('send_message', (sender, receiver, msg, date) => {
        var newMessage = new Message(sender, receiver, msg, date)
        if(connectedUsers[receiver] != undefined)
        {        
            var receiverSocketId = connectedUsers[receiver]
<<<<<<< HEAD
            console.log('CONNECTED USER ARRAY: ')
            console.log(connectedUsers)
            console.log('MESSAGE SENDING TO: ' + receiverSocketId)
=======
>>>>>>> f998f98166c518ea2e075e66312dd2d1281299f0
        
            io.to(receiverSocketId).emit("new_message", msg, date)
            io.to(receiverSocketId).emit('display_last_message', sender, msg, date)
        }
        newMessage.Save()
<<<<<<< HEAD
        //add to db
    })

    socket.on('destroy_app', (userPhone) => {
        console.log('The application is closing, array before: ')
        console.log(connectedUsers)

        console.log('After: ')
        delete connectedUsers[userPhone] 
        console.log(connectedUsers)
=======
>>>>>>> f998f98166c518ea2e075e66312dd2d1281299f0
    })

    socket.on('is_typing', (isTyping, receiver) =>
    {
        var receiverSocketId = connectedUsers[receiver]
        io.to(receiverSocketId).emit("user_is_typing", isTyping)
    })

    socket.on('disconnection', (userPhone) => {
<<<<<<< HEAD
        console.log('=======disconnection=======')
        console.log(connectedUsers)
        console.log('Connected users before deleting: ')
        console.log(connectedUsers[userPhone]) 

        delete connectedUsers[userPhone]   
        delete usersCookies[userPhone]


        console.log(connectedUsers[userPhone]) 
        console.log('Connected users after deleting: ')
        console.log(connectedUsers)
        console.log('User disconnected ' + socket.id + "," + userPhone)  
        console.log('=======disconnection=======') 
=======
        delete connectedUsers[userPhone]   
        delete usersCookies[userPhone]
>>>>>>> f998f98166c518ea2e075e66312dd2d1281299f0
    })
}

function GetSessionId(cookie) {
    var endPos = cookie.indexOf('.')
    var cookieId = cookie.slice(2, endPos)
    return cookieId;
}