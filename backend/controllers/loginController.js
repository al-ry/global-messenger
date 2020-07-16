exports.ParseLoginData = (req, res) => {
    var userData = req.body
    var telephone = userData.telephone
    var password = userData.password
    console.log('body is ' + userData)
    console.log('telephone is ' + telephone)
    console.log('password is ' + password)   
    res.send('success')
}