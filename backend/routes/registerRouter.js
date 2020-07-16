const { Router } = require('express')
const registerRouter = Router()
const registerController = require('../controllers/registerController.js')

var bodyParser = require('body-parser')
registerRouter.use(bodyParser.json())
registerRouter.use(bodyParser.urlencoded({extended : true}))

registerRouter.post('/register', registerController.ParseRegisterData)

module.exports = registerRouter