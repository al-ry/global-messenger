const { Router } = require('express')
const getHistoryRouter = Router()
const messageHistoryController = require('../controllers/messageHistoryController')

getHistoryRouter.get('/getHistory', messageHistoryController.Get)

module.exports = getHistoryRouter

