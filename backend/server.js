
var express = require('express')
var app = express()
var bodyParser = require('body-parser')
var registerRouter = require('./routes/registerRouter')
var loginRouter = require('./routes/loginRouter')

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended : true}))

app.use(registerRouter)
app.use(loginRouter)

app.listen('3000', () => {
    console.log('Server started on port 3000...')

})

