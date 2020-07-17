var User = require('../models/user.js')

exports.ParseRegisterData = (req, res) => {
    var userData = req.body
    var telephone = userData.telephone
    var password = userData.password
    var name = userData.name
    var newUser = new User(name, telephone, password);
    console.log('telephone is ' + newUser.telephone)
    console.log('password is ' + newUser.password)
    console.log('name is ' + newUser.name)  
    newUser.Find(function(result) {
        if (result) {
            res.send('user already exists. try again');
        } else {
            res.send('user is not exist')
            newUser.Register()
        }
    })

}