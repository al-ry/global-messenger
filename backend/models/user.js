var passwordUtil = require('../utils/passwordUtil')
var dbHelper = require('../db/database')
const { GetHome } = require('../controllers/homeController')
const { GetUserIdByNumber } = require('../db/database')


module.exports = class User {
  
    constructor(name, telephone, password) {
        this.name = name
        this.password = password
        this.telephone = telephone
    }
    Register() {
        var hashData = passwordUtil.SaltHashPassword(this.password)
        dbHelper.InsertNewUser(this.telephone, this.name, hashData.passwordHash, hashData.salt)
    }
    static FindOne(telephone) {
        return new Promise((resolve) => {
            dbHelper.GetUserInfoByNumber(telephone).then((result) => {
                resolve(result)
            })
        })
    }
    static Search(telephone) {
        return new Promise((resolve) => {
            dbHelper.GetUsersInfoByNumber(telephone).then((result) => {
                resolve(result)
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
        console.log(userId, friendId)
        return new Promise((resolve) => {
            dbHelper.CheckHasChat(userId, friendId).then((hasChat) => {
                if (hasChat) {
                    resolve(false)
                } else {
                    dbHelper.InsertNewChat(userId, friendId) 
                    resolve(true)
                }
            })
        })
    }

    static GetChatList(userId) {
        return new Promise((resolve) => {
            dbHelper.GetChatList(userId).then((result) => {
                resolve(result)
            })
        })
    }
    // static DeleteChat(userId, friendId) {
    //     var prepSql = db.prepare('DELETE FROM user_has_friend WHERE (id_user = ?) AND (id_friend = ?);')
    //     return new Promise((resolve) => {
    //         db.serialize(() => {
    //             prepSql.run(userId, friendId)
    //             resolve();
    //         })
    //     })
    // }
}