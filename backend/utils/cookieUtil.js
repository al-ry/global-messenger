var cookie = require('cookie-signature');
var globals = require('../config/config')

function SignCookie(token) {
    return `s:${cookie.sign(token, globals.SESSION_SECRET)}`;
}

module.exports =  {SignCookie}
