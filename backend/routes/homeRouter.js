const { Router } = require('express')
const homeRouter = Router()
const homeController = require('../controllers/homeController')

homeRouter.get('/home', homeController.GetHome)

module.exports = homeRouter