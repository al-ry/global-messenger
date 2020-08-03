const { Router } = require('express')
const loginRouter = Router()
const loginController = require('../controllers/loginController.js')


loginRouter.post('/login', loginController.Login)

module.exports = loginRouter