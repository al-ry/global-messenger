const { Router } = require('express')
const allUsersRouter = Router()
const allUsersController = require('../controllers/allUsersController')

registerRouter.post('/allUsers', registerController.ShowAll)

module.exports = registerRouter