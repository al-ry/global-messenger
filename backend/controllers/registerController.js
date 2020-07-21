var User = require('../models/user.js')

exports.Register = (req, res) => {
    var userData = req.body
    var newUser = new User(userData.name, userData.telephone, userData.password);
    User.Find(userData.telephone, function(result) {
        if (result) {
            res.status(400).send('login is busy')
        } else {
            newUser.Register()
            User.Find(newUser.telephone, function(newResult) {
                req.session.user = newResult
                console.log(newResult)
                console.log(req.session.user)
            })
            res.status(200).send('You are successfully registrated')
        }
    })
}