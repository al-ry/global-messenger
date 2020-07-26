const { Router } = require('express')
const deleteChatRouter = Router()
const deleteChatController = require('../controllers/deleteChatController')

deleteChatRouter.get('/deleteChat', deleteChatController.Delete)

module.exports = deleteChatRouter