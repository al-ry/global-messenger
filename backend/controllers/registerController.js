var bodyParser = require('body-parser')



exports.ParseUserData = (req, res) => {
    var userData = req.body
    console.log('hello ' + userData.password)  
    
}