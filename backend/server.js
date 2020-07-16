var db = require('./db/database.js')

var express = require('express')
var app = express()
var server = require('http').createServer(app)
var crypto = require('crypto')
var uuid = require('uuid')
var bodyParser = require('body-parser')

var registerRouter = require('./routes/registerRouter')
var loginRouter = require('./routes/loginRouter')

app.use(bodyParser.json())
app.use(require('connect').bodyParser());
app.use(bodyParser.urlencoded({extended : true}))

app.use(registerRouter)
app.use(loginRouter)

var GetRandomString = function(length) {
    return crypto.randomBytes(Math.ceil(length/2)).toString('hex').slice(0, length)
}

var SHA512 = function(password, salt)
{
    var hash = crypto.createHmac('sha512', salt)
    hash.update(password)
    var value = hash.digest('hex')
    return {
        salt : salt,
        passwordHash : value
    }
}

function SaltHashPassword(userPassword){
    var salt = GetRandomString(16)
    var passwordData = SHA512(userPassword, salt)
    return passwordData
}

app.listen('3000', () => {
    console.log('Server started on port 3000...')
})


