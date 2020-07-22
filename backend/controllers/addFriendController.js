var User = require('../models/user.js')

exports.Add = (req, res) => {
    var userData = req.query
    console.log(userData.userId, userData.friendId)
    User.AddFriend(userData.userId, userData.friendId)
    res.send('added')
 }