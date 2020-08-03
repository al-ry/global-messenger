var User = require('../models/user.js')

exports.GetHome = (req, res) => {
<<<<<<< Updated upstream
    console.log('================home')
    console.log(req.session)
    console.log(req.cookies)
    console.log(req.session.user)   
    console.log('================home')
    if (req.session.user) {
        userId = req.session.user.id_user
        User.GetChatList(userId).then((result) =>{
            res.status(200).json(result)
        })
=======
    if (req.session.user) {       
        res.status(200).send('success')
>>>>>>> Stashed changes
    } else {
        res.status(400).send('fail')
    }
}