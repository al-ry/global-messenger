const { Router } = require('express')
const findUserRouter = Router()
const allUsersController = require('../controllers/findUserController')

findUserRouter.get('/findUser', allUsersController.FindOne)

module.exports = findUserRouter