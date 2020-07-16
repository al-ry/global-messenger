var sqlite3 = require('sqlite3').verbose();

var db = new sqlite3.Database('messenger.sqlite3');

//db.serialize(function() {
//    var prepare = db.prepare('INSERT INTO test(name) VALUES (?)');
//    prepare.run('Alexander');
//})

exports.FindUser = (user) => {

}

exports.RegisterNewUser = (user) => {
    
}