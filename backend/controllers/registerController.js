var User = require('../models/user.js')

exports.ParseRegisterData = (req, res) => {
    var userData = req.body
    var telephone = userData.telephone
    var password = userData.password
    var name = userData.name
    console.log('telephone is ' + telephone)
    console.log('password is ' + password)  
    console.log('name is ' + name)  
    res.send('success') 
    //var newUser = new User(name, telephone, password);
}