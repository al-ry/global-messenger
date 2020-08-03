var User = require('../models/user.js')
var cookieUtil = require('../utils/cookieUtil')

exports.Register = (req, res) => {
    var userData = req.body
    var newUser = new User(userData.name, userData.telephone, userData.password);
    User.FindOne(newUser.telephone).then((result) => {
        if (result) {
            res.status(400).send('login is busy')
        } else {
            newUser.Register()
            User.FindOne(newUser.telephone).then(newUser=> {
                req.session.user = newUser
                var newCookie = cookieUtil.SignCookie(req.sessionID);
                req.cookies['connect.sid'] = newCookie
                res.status(200).json(req.cookies)
            })
        }
    })
}