var User = require('../models/user.js')

exports.GetChatList = (req, res) => {
    queryData = req.query;
    console.log(queryData.userId)
    console.log('here')
    User.GetChatList(queryData.userId).then((result) => {
        res.status(200).json(result)
    })
}