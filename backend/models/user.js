var db = require('../db/database.js')
var pass = require('../config/authConfig.js')


module.exports = class User {
  
    constructor(name, telephone, password) {
        this.name = name
        this.password = password
        this.telephone = telephone
    }
    Register() {
        var hashData = pass.SaltHashPassword(this.password)
        console.log(hashData.passwordHash)
        var prep = db.prepare('INSERT INTO user(telephone, name, crypted_password, salt_password)'
         + 'VALUES (?, ?, ?, ?)')
        prep.run(this.telephone, this.name, hashData.passwordHash, hashData.salt)
    }

    Find(callback) {
        var prep = db.prepare('SELECT * FROM user WHERE telephone = ?')  
        prep.get(this.telephone, function(err, result) {
            if (err) throw err
            console.log(result)
            callback(result)
        })
    }

        
}