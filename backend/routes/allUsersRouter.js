const { Router } = require('express')
const allUsersRouter = Router()
const allUsersController = require('../controllers/allUsersController')

allUsersRouter.get('/allUsers', allUsersController.GetAll)

module.exports = allUsersRouter