var User = require('../models/user.js')

exports.Add = (req, res) => {
<<<<<<< Updated upstream
    var userData = req.query
    console.log(userData.userId, userData.friendId)
    User.AddChat(userData.userId, userData.friendId).then((isAdded) => {
        if (isAdded)
        {
=======
    var queryData = req.query
    User.AddChat(queryData.userPhone, queryData.friendPhone).then((isAdded) => {
        if (isAdded) {
>>>>>>> Stashed changes
            res.status(200).send('chat is added')
        } else {
            res.status(400).send('chat already added')
        }
    })
 }