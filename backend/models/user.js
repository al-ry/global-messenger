var pass = require('../config/authConfig.js')
var db = require('../db/database')


module.exports = class User {
  
    constructor(name, telephone, password) {
        this.name = name
        this.password = password
        this.telephone = telephone
    }
    async Register() {
        var hashData = await pass.SaltHashPassword(this.password)
        var prep = db.prepare('INSERT INTO user(telephone, name, crypted_password, salt_password)'
         + 'VALUES (?, ?, ?, ?)')
        await prep.run(this.telephone, this.name, hashData.passwordHash, hashData.salt)
    }

    static async Find(telephone, callback) {
        var prep = db.prepare('SELECT * FROM user WHERE telephone = ?')  
        prep.get(telephone, await function(err, result) {
            if (err) throw err
            callback(result)
        })
    }  
    static async CheckPassword(password, encryptedPassword, salt) {
        var hashedPassword = await pass.CheckHashPassword(password, salt).passwordHash
        if (encryptedPassword == hashedPassword) {
            return true;
        }
        return false;
    }     
}