const { Router } = require('express')
const registerRouter = Router()
const registerController = require('../controllers/registerController.js')

registerRouter.post('/register', registerController.Register)

module.exports = registerRouter