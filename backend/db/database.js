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

var GetUsersIdByNumber = function(userPhone, friendPhone) {
    return new Promise(resolve => {
        db.serialize(() => {
            var prep = db.prepare('')
            db.get('SELECT id_user FROM user WHERE telephone = ?;', userPhone, (err, user) => {
                db.get('SELECT id_user FROM user WHERE telephone = ?;', friendPhone, (err, friend) => {
                    var usersIds = {userId: user.id_user, friendId: friend.id_user}
                    resolve(usersIds)
                })
            })
        })
    })
}

var GetChatList = function (userPhone) {
    var prepSql = db.prepare('SELECT telephone, name FROM\n' +
    '(SELECT user.id_user AS userId, id_friend AS friendId FROM user\n' +
    'INNER JOIN user_has_friend ON user.id_user = user_has_friend.id_user\n' +
    'WHERE user.telephone = ?)\n' +
    'INNER JOIN user ON user.id_user = friendId;\n')
    return new Promise((resolve) => {
        db.serialize(() => {
            prepSql.all(userPhone, (err, result) => {
                resolve(result)
            })
        })
    })
}

var CheckHasChat = function(userPhone, friendPhone) {
    return new Promise(resolve => {
        db.serialize(() => {
            var prep = db.prepare('SELECT userId, friendId FROM\n' +
            '(SELECT user.id_user AS userId, id_friend AS friendId FROM user\n' + 
            'INNER JOIN user_has_friend ON user.id_user = user_has_friend.id_user\n' +
            'WHERE user.telephone = ?)\n' +
            'INNER JOIN user ON user.id_user = friendId\n' +
            'WHERE telephone = ?')
            prep.get(userPhone, friendPhone, (err, result) => {
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
                 GetUsersIdByNumber,
                 InsertNewChat,
                 GetChatList
                };