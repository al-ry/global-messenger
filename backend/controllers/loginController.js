var User = require('../models/user.js')


exports.Login = (req, res) => {
    var userData = req.body
    User.Find(userData.telephone, function(result) {
        if (result)
        {
            if(User.CheckPassword(userData.password, result.crypted_password, result.salt_password) == true)
            {
                console.log('================login')
                req.session.user = result
                console.log(req.session)
                console.log(req.sessionID)
                console.log(req.cookies)
                console.log(req.session.cookie)
                console.log('================login')  
                //clearCookie('cookies_to_client').cookie('cookies_to_client', req.cookies)
                res.status(200).cookie('cookies_to_client', 'new cookie').clearCookie('cookies_to_client').send('cookie has sent')

                //res.status(200).send(req.cookies)
            } else {
                res.status(400).send('password is incorrect')
            }
        } else {
            res.status(400).send('login is incorrect')
        }
    })
}