const { Router } = require('express')
const addChatRouter = Router()
const addChatController = require('../controllers/addChatController')

addChatRouter.get('/addChat', addChatController.Add)

module.exports = addChatRouter