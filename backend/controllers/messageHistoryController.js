var dbHelper = require('../db/database')

exports.Get = (req, res) => {
    var queryData = req.query
    dbHelper.GetMessageHistory(queryData.senderPhone, queryData.receiverPhone).then((result) => {
        res.status(200).json(result);
    })
}
