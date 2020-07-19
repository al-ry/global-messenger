var User = require('../models/user.js')

exports.Register = (req, res) => {
    var userData = req.body
    var newUser = new User(userData.name, userData.telephone, userData.password);
    User.Find(userData.telephone, function(result) {
        if (result) {
            res.send('login is busy')
        } else {
            res.send('new login')
            newUser.Register()
        }
    })
}