const { Router } = require('express')
const registerRouter = Router()
const registerController = require('../controllers/registerController.js')
var bodyParser = require('body-parser')

registerRouter.post('/register', (req, res) => {
    registerController.ParseUserData(req, res)
})

module.exports = registerRouter