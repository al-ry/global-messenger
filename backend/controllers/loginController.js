var User = require('../models/user.js')
var cookie = require('cookie-signature');
function signCookie(token) {
    return `s:${cookie.sign(token, 'any secret string')}`;
   }

exports.Login = (req, res) => {
    var userData = req.body
    User.Find(userData.telephone, function(result) {
        if (result)
        {
            if(User.CheckPassword(userData.password, result.crypted_password, result.salt_password) == true)
            {
                console.log('================login')
                req.session.user = result
                var newCookie = signCookie(req.sessionID);
                console.log(newCookie)
                req.cookies['connect.sid'] = newCookie
                console.log('================login')
                //clearCookie('cookies_to_client').cookie('cookies_to_client', req.cookies)
                res.status(200).send(req.cookies)

                //res.status(200).send(req.cookies)
            } else {
                res.status(400).send('password is incorrect')
            }
        } else {
            res.status(400).send('login is incorrect')
        }
    })
}