var cookie = require('cookie-signature');
var globals = require('../config/globals')

function SignCookie(token) {
    return `s:${cookie.sign(token, globals.SESSION_SECRET)}`;
}

module.exports =  {SignCookie}
