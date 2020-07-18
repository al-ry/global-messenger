const { Router } = require('express')
const homeRouter = Router()
const homeController = require('../controllers/homeController')


loginRouter.get('/home', homeController.RenderHome)

module.exports = loginRouter