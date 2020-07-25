var User = require('../models/user.js')

exports.Search = (req, res) => {
    var searchData = req.query
    console.log(req.query)
    User.Search(searchData.telephone).then((result) => {
        if (result)
        {
            res.status(200).json(result)
        } else {
            res.status(404).send('User not Found')
        }      
    })
}