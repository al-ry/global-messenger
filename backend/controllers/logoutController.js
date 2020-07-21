

exports.Logout = (req, res) => {
    if (req.session.user) {
        req.session.destroy(() => {
            res.status(200).send('You are logged out')
        })
    }
}