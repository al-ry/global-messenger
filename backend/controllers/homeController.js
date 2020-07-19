
exports.Home = (req, res) => {
    var user = req.session.user 
    console.log('home')
    if (user)
    {
        console.log('success')
        res.status(200)
    } else {
        console.log('fail')
        res.status(404)
    }
}