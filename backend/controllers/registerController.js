var User = require('../models/user.js')

exports.Register = (req, res) => {
    var userData = req.body
    var newUser = new User(userData.name, userData.telephone, userData.password);
    User.Find(userData.telephone, function(result) {
        if (result) {
            res.status(400).send('login is busy')
        } else {
            newUser.Register()
            res.status(200).send('You are successfully registrated')
        }
    })
}