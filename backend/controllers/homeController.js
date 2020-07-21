
exports.Home = (req, res) => {
    console.log('======================')
    console.log(req.session.user)
    if (req.session.user)
    {
        console.log('success')
        res.status(200).send('success')
    } else {
        console.log('fail')
        res.status(400).send('fail')
    }
}