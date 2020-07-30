var cookie = require('cookie-signature');
var constants = require('../config/constants')

function SignCookie(token) {
    return `s:${cookie.sign(token, constants.SESSION_SECRET)}`;
}

module.exports =  {SignCookie}
