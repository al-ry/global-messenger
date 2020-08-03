session = require('express-session')
var SQLiteStore = require('connect-sqlite3')(session)

module.exports = new SQLiteStore({db: 'sessionDB'})
