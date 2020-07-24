const { Router } = require('express')
const addChatRouter = Router()
const addChatController = require('../controllers/addChatController')

addChatRouter.get('/addFriend', addChatController.Add)

module.exports = addChatRouter