
exports.GetHome = (req, res) => {
    if (req.session.user) {       
        res.status(200).send('success')
    } else {
        res.status(400).send('fail')
    }
}