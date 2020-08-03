var User = require('../models/user.js')

exports.Search = (req, res) => {
    var searchData = req.query
    User.Search(searchData.telephone).then((result) => {
        res.status(200).json(result)
    })
}