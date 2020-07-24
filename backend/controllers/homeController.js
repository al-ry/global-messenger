var User = require('../models/user.js')

exports.Home = (req, res) => {
    console.log('================home')
    console.log(req.session)
    console.log(req.cookies)
    console.log(req.session.user)   
    console.log('================home')
    userId = req.session.user.id_user
    if (req.session.user) {
        User.GetChatList(userId).then((result) =>{
            res.status(200).json(result)
        })
    } else {
        res.status(400).send('fail')
    }
}