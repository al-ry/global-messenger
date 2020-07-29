session = require('express-session')
var SQLiteStore = require('connect-sqlite3')(session)
var config = module.exports = {}

config.SESSION_SECRET = 'any secret string'

config.sessionDB = new SQLiteStore({db: 'sessionDB'})