
exports.Home = (req, res) => {
    console.log(req.session.user)
    if (req.session.user)
    {
        console.log('success')
        res.send('success')
    } else {
        console.log('fail')
        res.send('fail')
    }
}