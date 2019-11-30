var express = require('express');
var app = express();
var server = require('http').createServer(app);
var socket = require('socket.io').listen(server);
var mysql = require('mysql')

app.listen('3000', () => {
    console.log('Server started on port 3000...')
});

app.get('/', (req, res) => {
    console.log("Responde to root")
    res.send('server is running')
})

app.get('/users:id', (req, res) => {
    console.log("Responde to root")
    res.send('Nodemon updates')
})

var connection = mysql.createConnection({
    host: "localhost",
    user: "root",
    port: 3000,
    database: "localhost",
})

/*
var connection = mysql.createConnection({
    host: "127.0.0.1",
    user: "root",
    port: 3000,
    password: '',
    database: "messanger_db",
});

connection.connect((error) => {
    if(error) throw error;   
    console.log("connected");
});

//create db;

app.get('/createdb', (req, res) => {
    let sql = 'create database';
    db.query(sql, (err, result) => {
        if(error) throw error;
        console.log(result);
        res.send('database created');
    });
} ); */
