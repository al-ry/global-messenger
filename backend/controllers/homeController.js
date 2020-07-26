var User = require('../models/user.js')

exports.GetHome = (req, res) => {
    console.log('================home')
    console.log(req.session)
    console.log(req.cookies)
    console.log(req.session.user)   
    console.log('================home')
    if (req.session.user) {       
        console.log(req.session.user)
        var user = {
            id_user: req.session.user.id_user,
            name: req.session.user.name,
            telephone: req.session.user.telephone
        }
        res.status(200).json(user)
    } else {
        res.status(400).send('fail')
    }
}