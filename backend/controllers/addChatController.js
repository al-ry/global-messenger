var User = require('../models/user.js')

exports.Add = (req, res) => {
    var queryData = req.query

    User.AddChat(queryData.userPhone, queryData.friendPhone).then((isAdded) => {
        if (isAdded) {
            res.status(200).send('chat is added')
        } else {
            res.status(400).send('chat already added')
        }
    })
 }