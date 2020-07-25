var User = require('../models/user.js')
var cookieUtil = require('../utils/cookieUtil')

exports.Login = (req, res) => {
    var userData = req.body
    User.FindOne(userData.telephone).then(result => {
        if (result)
        {
            if(User.CheckPassword(userData.password, result.crypted_password, result.salt_password) == true)
            {
                req.session.user = result
                var newCookie = cookieUtil.SignCookie(req.sessionID)
                req.cookies['connect.sid'] = newCookie
                res.status(200).json(req.cookies)
            } else {
                res.status(400).send('password is incorrect')
            }
        } else {
            res.status(400).send('login is incorrect')
        }
    })
}