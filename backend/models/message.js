var dbHelper = require('../db/database')


module.exports = class Message {
    constructor(from, to, msg, time) {
        this.from = from
        this.to = to
        this.message = msg
        this.time = time 
    }

    Save() {
        dbHelper.InsertNewMessage(this.from, this.to, this.message, this.time)
    }
}