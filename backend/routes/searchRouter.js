const { Router } = require('express')
const searchUserRouter = Router()
const findUserController = require('../controllers/searchController')

searchUserRouter.get('/search', findUserController.Search)

module.exports = searchUserRouter