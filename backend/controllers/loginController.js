var User = require('../models/user.js')

exports.Login = (req, res) => {
    var userData = req.body
    User.Find(userData.telephone, function(result) {
        if (result)
        {
            if(User.CheckPassword(userData.password, result.crypted_password, result.salt_password) === true)
            {  
                res.send('data is correct')
            } else {
                res.send('data is incorrect')
            }
        } else {
            res.send('login is wrong')
        }
    })

}