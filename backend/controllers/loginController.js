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
                res.json(req.session)
            } else {
                res.send('data is incorrect')
            }
        } else {
            res.send('login is wrong')
        }
    })

}