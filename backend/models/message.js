var dbHelper = require('../db/database')


module.exports = class Message {
    constructor(from, to, msg, time) {
        this.from = from
        this.to = to
        this.message = msg
        this.time = time 
    }

    Save() {
        dbHelper.GetUsersIdByNumber(this.from, this.to).then((result) => {
            dbHelper.InsertNewMessage(result.userId, result.friendId, this.message, this.time)
        })
    }
}