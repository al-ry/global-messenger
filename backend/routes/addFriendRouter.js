const { Router } = require('express')
const addFriendRouter = Router()
const addFriendController = require('../controllers/addFriendController')

addFriendRouter.get('/addFriend', addFriendController.Add)

module.exports = addFriendRouter