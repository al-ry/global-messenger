
exports.Home = (req, res) => {
    console.log('================home')
    console.log(req.session)
    console.log(req.cookies)
    console.log(req.session.user)   
    console.log('================home')
    if (req.session.user)
    {
        console.log('success')
        res.send('success')
    } else {
        console.log('fail')
        res.send('fail')
    }
}