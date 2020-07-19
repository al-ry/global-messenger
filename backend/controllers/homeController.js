
exports.Home = (req, res) => {
    var user = req.session.user 
    console.log('home')
    if (user)
    {
        console.log('home')
        res.status(200)
    } else {
        console.log('home')
        res.status(404)
    }
}