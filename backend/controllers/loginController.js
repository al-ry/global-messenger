var User = require('../models/user.js')


exports.Login = (req, res) => {
    var userData = req.body
    User.Find(userData.telephone, function(result) {
        if (result)
        {
            if(User.CheckPassword(userData.password, result.crypted_password, result.salt_password) == true)
            {
                req.session.user = result
                console.log(req.session.user)
                res.status(200).send('You are logged in')
            } else {
                res.status(400).send('password is incorrect')
            }
        } else {
            res.status(400).send('login is incorrect')
        }
    })
}