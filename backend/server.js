var express = require('express')
var app = express()
var bodyParser = require('body-parser')
var registerRouter = require('./routes/registerRouter')
var loginRouter = require('./routes/loginRouter')
var session = require('express-session')
var liteStore = require('connect-sqlite3')(session)
app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended : true}))

app.use(registerRouter)
app.use(loginRouter)
//app.use(express.cookieParser())
//app.use(express.session())
// app.use(session({
//     secret: 'secret',
//     resave: false,
//     saveUninitialized: false,
//     cookie: {
//         maxAge: 60 * 30 * 1000
//     }
// }))


app.listen('3000', () => {
    console.log('Server started on port 3000...')

})

var pass = require('./config/authConfig.js')

app.get('/', (res, req) => {
    var crypt = pass.SaltHashPassword('new');
    console.log(crypt.passwordHash)
    var passsword = pass.CheckHashPassword('new', crypt.salt)
    console.log(passsword.passwordHash)
    req.send('hello')
})
