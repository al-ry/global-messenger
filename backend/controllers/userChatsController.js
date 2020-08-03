var User = require('../models/user.js')

exports.GetChatList = (req, res) => {
    queryData = req.query;
    console.log(queryData.userPhone)
    User.GetChatList(queryData.userPhone).then((result) => {
        res.status(200).json(result)
    })
}