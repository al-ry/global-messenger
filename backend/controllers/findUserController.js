var User = require('../models/user.js')

exports.FindOne = (req, res) => {
    var userData = req.query
    console.log(req.query)
    User.Find(userData.telephone, (result) => {
        if (result)
        {
            res.status(200).json(result)
        } else {
            res.status(404).send('User not Found')
        }      
    })
}