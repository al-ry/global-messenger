var passwordUtil = require('../utils/passwordUtil')
var db = require('../db/database')


module.exports = class User {
  
    constructor(name, telephone, password) {
        this.name = name
        this.password = password
        this.telephone = telephone
    }
    Register() {
        var hashData = passwordUtil.SaltHashPassword(this.password)
        var prep = db.prepare('INSERT INTO user(telephone, name, crypted_password, salt_password)'
         + 'VALUES (?, ?, ?, ?)')
        await prep.run(this.telephone, this.name, hashData.passwordHash, hashData.salt)
    }

    static Find(telephone, callback) {
        var prep = db.prepare('SELECT * FROM user WHERE telephone = ?')  
        prep.get(telephone, function(err, result) {
            if (err) throw err
            callback(result)
        })
    }  
    static CheckPassword(password, encryptedPassword, salt) {
        var hashedPassword = passwordUtil.CheckHashPassword(password, salt).passwordHash
        if (encryptedPassword == hashedPassword) {
            return true
        }
        return false
    } 
    static AddFriend(userId, friendId) {
        var prep = db.prepare('SELECT * FROM user_has_friend WHERE (id_user = ?) AND (id_friend = ?)')
        prep.get(userId, friendId, function(result, err) {
            
        })
        var prep = db.prepare('INSERT INTO user_has_friend(id_user, id_friend)'
        + 'VALUES (?, ?)')
      prep.run(userId, friendId)
    }
    static async GetFriendList(userId) {

    }
}