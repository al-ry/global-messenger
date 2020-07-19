var User = require('../models/user.js')

exports.GetAll = (req, res) => {
    User.GetAll((result) => {
        if (result)
        {
            res.json(result)
        } else {
            res.send('no users')
        }      
    })
}