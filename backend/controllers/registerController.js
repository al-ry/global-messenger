var bodyParser = require('body-parser')

exports.ParseUserData = (req, res) => {
    var userData = req.body
    var telephone = userData.telephone
    var password = userData.password
    var name = userData.name 
}