var passwordUtil = require('../utils/passwordUtil')
var dbHelper = require('../db/database')

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
    static AddChat(userPhone, friendPhone) {
        return new Promise((resolve) => {
            dbHelper.CheckHasChat(userPhone, friendPhone).then((hasChat, result) => {
                if (hasChat) {
                    resolve(false)
                } else {
                    dbHelper.GetUsersIdByNumber(userPhone, friendPhone).then(result => {
                        dbHelper.InsertNewChat(result.userId, result.friendId) 
                        resolve(true)
                    })
                }
            })
        })
    }

    static GetChatList(userPhone) {
        return new Promise((resolve) => {
            dbHelper.GetChatList(userPhone).then((result) => {
                resolve(result)
            })
        })
    }
    static DeleteChat(userPhone, friendPhone) {
        return new Promise((resolve) => {
            dbHelper.GetUsersIdByNumber(userPhone, friendPhone).then(result => {
                dbHelper.DeleteChat(result.userId, result.friendId)
                resolve(true)
            })
        })
    }
}