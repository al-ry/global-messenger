var express = require('express')
var app = express()
var bodyParser = require('body-parser')
var cookieParser = require('cookie-parser')
var registerRouter = require('./routes/registerRouter')
var loginRouter = require('./routes/loginRouter')
var allUsersRouter = require('./routes/allUsersRouter')
var homeRouter = require('./routes/homeRouter')
var session = require('express-session');
var SQLiteStore = require('connect-sqlite3')(session);


app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended : true}))
app.set('trustproxy', true)
app.use(cookieParser())
app.use(session({
    secret: 'any secret string',
    resave: true,
    saveUninitialized: true,
    store: new SQLiteStore,
    cookie: {
        maxAge: 60 * 30 * 1000
    }
}))
app.use(registerRouter)
app.use(loginRouter)
app.use(allUsersRouter)
app.use(homeRouter)

app.listen('3000', () => {
    console.log('Server started on port 3000...')
})
app.get('/', (req, res) => {
    res.send('hello')
})