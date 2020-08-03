var User = require('../models/user.js')

exports.GetChatList = (req, res) => {
    queryData = req.query;
    User.GetChatList(queryData.userPhone).then((result) => {
        res.status(200).json(result)
    })
}