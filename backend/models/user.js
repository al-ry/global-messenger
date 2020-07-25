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
        db.serialize(() => {     
            var prep = db.prepare('INSERT INTO user(telephone, name, crypted_password, salt_password)'
            + 'VALUES (?, ?, ?, ?);')
            prep.run(this.telephone, this.name, hashData.passwordHash, hashData.salt)
        })
    }
    static FindOne(telephone) {
        return new Promise((resolve) => {
            db.serialize(() => {
                var prep = db.prepare('SELECT * FROM user WHERE telephone = ?;')
                prep.get(telephone, (err, result) => {
                    resolve(result);
                })
            })
        })
    }
    static Search(telephone) {
        return new Promise((resolve) => {
            db.serialize(() => {
                var prep = db.prepare('SELECT id_user, name, telephone FROM user WHERE telephone LIKE ?;')
                const param = telephone + '%'
                prep.all(param, (err, result) => {
                    resolve(result);
                })
            })
        })
    }  
    static CheckPassword(password, encryptedPassword, salt) {
        var hashedPassword = passwordUtil.CheckHashPassword(password, salt).passwordHash
        if (encryptedPassword == hashedPassword) {
            return true
        }
        return false
    } 
    static AddChat(userId, friendId) {
        return new Promise((resolve) => {
            db.serialize(() => {
                var prep = db.prepare('SELECT * FROM user_has_friend WHERE (id_user = ?) AND (id_friend = ?);')
                prep.get(userId, friendId, (err, result) => {
                    if (result) {
                        resolve(false)
                    } else {
                        var prep = db.prepare('INSERT INTO user_has_friend(id_user, id_friend) VALUES (?, ?);')
                        prep.run(userId, friendId)
                        resolve(true)                       
                    }
                })
            })
        })
    }
    static GetChatList(userId) {
        var prepSql = db.prepare('SELECT user.name, user.telephone FROM user\n' +
        'INNER JOIN user_has_friend ON user_has_friend.id_friend = user.id_user\n' +
        'WHERE user_has_friend.id_user = ?;\n')
        return new Promise((resolve) => {
            db.serialize(() => {
                prepSql.all(userId, (err, result) => {
                    resolve(result)
                })
            })
        })
    }
    static DeleteChat(userId, friendId) {
        var prepSql = db.prepare('DELETE FROM user_has_friend WHERE (id_user = ?) AND (id_friend = ?);')
        return new Promise((resolve) => {
            db.serialize(() => {
                prepSql.run(userId, friendId)
                resolve();
            })
        })
    }
}