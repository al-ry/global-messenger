var User = require('../models/user.js')

exports.Add = (req, res) => {
    var userData = req.query
    console.log(userData.userId, userData.friendId)
    User.AddChat(userData.userId, userData.friendId).then((isAdded) => {
        if (isAdded)
        {
            res.status(200).send('friend is added')
        } else{
            res.status(400).send('friend already added')
        }
    })
 }