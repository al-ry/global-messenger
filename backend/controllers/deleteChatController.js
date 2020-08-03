var User = require('../models/user.js')

exports.Delete = (req, res) => {
    var params = req.query
    User.DeleteChat(params.userPhone, params.friendPhone).then(() => {
        res.status(200).send('Chat successfully deleted')
    })
}
