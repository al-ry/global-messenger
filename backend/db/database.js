var sqlite3 = require('sqlite3').verbose();

var db = new sqlite3.Database('messenger.sqlite3');



var InsertNewUser = (telephone, name, password, salt) => {
    db.serialize(() => {     
        var prep = db.prepare('INSERT INTO user(telephone, name, crypted_password, salt_password)'
        + 'VALUES (?, ?, ?, ?);')
        prep.run(telephone, name, password, salt)
    })
}

var GetUserInfoByNumber = function(telephone) {
    return new Promise(resolve => {
        db.serialize(() => {
            var prep = db.prepare('SELECT * FROM user WHERE telephone = ?;')
            prep.get(telephone, (err, result) => {
                resolve(result);
            })
        })
    })
}

var GetUsersInfoByNumber = function(telephone) {
    return new Promise(resolve => {
        db.serialize(() => {
            var prep = db.prepare('SELECT id_user, name, telephone FROM user WHERE telephone LIKE ?;')
            const param = telephone + '%'
            prep.all(param, (err, result) => {
                resolve(result);
            })
        })
    })
}

var GetUserIdByNumber = function(userPhone) {
    return new Promise(resolve => {
        db.serialize(() => {
            db.get('SELECT id_user FROM user WHERE telephone = ?;', userPhone, (err,result) => {
                resolve(result)
            })
        })
    })
}

var GetChatList = function(userId) {
    var prepSql = db.prepare('SELECT user.id_user, user.name, user.telephone FROM user\n' +
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

var CheckHasChat = function(userId, friendId) {
    return new Promise(resolve => {
        db.serialize(() => {
            var prep = db.prepare('SELECT * FROM user_has_friend WHERE (id_user = ?) AND (id_friend = ?);')
            prep.get(userId, friendId, (err, result) => {
                if (err) throw err;
                if (result) { 
                    resolve(true)
                } else {
                    resolve(false)
                }
            })
        })
    })
}

var InsertNewChat = (userId, friendId) => {
    db.serialize(() => {     
        var prep = db.prepare('INSERT INTO user_has_friend(id_user, id_friend) VALUES (?, ?);')
        prep.run(userId, friendId,  (err, res) => {
            if (err) throw err;
        })
    })
}

module.exports = {GetUserInfoByNumber,
                 InsertNewUser,
                 GetUsersInfoByNumber,
                 CheckHasChat,
                 GetUserIdByNumber,
                 InsertNewChat,
                 GetChatList};