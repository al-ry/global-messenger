var crypto = require('crypto')

var GetRandomString = function(length) {
    return crypto.randomBytes(Math.ceil(length/2)).toString('hex').slice(0, length)
}
var SHA512 = function(password, salt)
{
    var hash = crypto.createHmac('sha512', salt)
    hash.update(password)
    var value = hash.digest('hex')
    return {
        salt : salt,
        passwordHash : value
    }
}
var SaltHashPassword = function(userPassword){
    var salt = GetRandomString(16)
    var passwordData = SHA512(userPassword, salt)
    return passwordData
}

module.exports = {SaltHashPassword}