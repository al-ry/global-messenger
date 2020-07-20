const { Router } = require('express')
const homeRouter = Router()
const homeController = require('../controllers/homeController')

homeRouter.post('/home', homeController.Home)

module.exports = homeRouter