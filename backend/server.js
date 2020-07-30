var express = require('express')
var session = require('express-session')
var bodyParser = require('body-parser')
var cookieParser = require('cookie-parser')

var cors = require('cors')

var constants = require('./config/constants')
var sessionDB = require('./db/sessions')
var registerRouter = require('./routes/registerRouter')
var loginRouter = require('./routes/loginRouter')
var searchRouter = require('./routes/searchRouter')
var homeRouter = require('./routes/homeRouter')
var logoutRouter = require('./routes/logoutRouter')
var addChatRouter = require('./routes/addChatRouter')
var userChatsRouter = require('./routes/userChatsRouter')
var deleteChatRouter = require('./routes/deleteChatRouter')
const socketManager = require('./socketManager')


var app = express()
var server = require('http').Server(app)


app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended : true}))
app.set('trust proxy', 1);
app.use(cors({ credentials: true, origin: true }))
app.use(cookieParser())


app.use(session({
    secret: constants.SESSION_SECRET,
    resave: true,
    saveUninitialized: true,
    store: sessionDB,
    cookie: { maxAge: constants.MAX_AGE, secure: true }
}))


app.use(loginRouter)
app.use(registerRouter)
app.use(searchRouter)
app.use(homeRouter)
app.use(logoutRouter)
app.use(addChatRouter)
app.use(deleteChatRouter)
app.use(userChatsRouter)


app.get('/', (req, res) => {
    res.sendFile('index.html', {root: __dirname})
})
const io = require("socket.io")(server)

var connectedUsers = [];
var usersCookies = [];

io.on('connection', function(socket) {

    console.log('User connected:' + socket.id)

    // connectedUsers.push(socket.id)
    // console.log(connectedUsers)

    socket.on('user_connected', (userPhone, cookie) => {
        if (usersCookies[userPhone] != undefined)
        {
            io.emit('log_out', cookie)
            var sessionId = GetSessionId(usersCookies[userPhone])
            sessionDB.destroy(sessionId)
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
})

function isUser(userList, userPhone) {
    return userPhone in userList;
}


function GetSessionId(cookie) {
    var endPos = cookie.indexOf('.')
    var cookieId = cookie.slice(2, endPos)
    return cookieId;
}

server.listen(3000, () => {
    console.log('Server started on port 3000...')
})



