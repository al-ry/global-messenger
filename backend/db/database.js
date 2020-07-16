var sqlite3 = require('sqlite3').verbose();

var db = new sqlite3.Database('test.sqlite3');

db.serialize(function() {
    var prepare = db.prepare('INSERT INTO test(name) VALUES (?)');
    prepare.run('Alexander');
})