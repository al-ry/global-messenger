const { Router } = require('express')
const searchUserRouter = Router()
const searchUserController = require('../controllers/searchController')

searchUserRouter.get('/search', searchUserController.Search)

module.exports = searchUserRouter