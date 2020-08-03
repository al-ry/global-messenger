const { Router } = require('express')
const userChatRouter = Router()
const userChatsController = require('../controllers/userChatsController')

userChatRouter.get('/chats', userChatsController.GetChatList)

module.exports = userChatRouter