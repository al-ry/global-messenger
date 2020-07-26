const { Router } = require('express')
const logoutRouter = Router()
const logoutController = require('../controllers/logoutController')


logoutRouter.get('/logout', logoutController.Logout)

module.exports = logoutRouter