const { Router } = require('express')
const getHistoryRouter = Router()
const getHistoryController = require('../controllers/getHistoryController')

getHistoryRouter.get('/getHistory', getHistoryController.Get)

module.exports = getHistoryRouter

