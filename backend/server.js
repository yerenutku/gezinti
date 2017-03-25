var express = require('express'),
session = require('express-session'),
app = express(),
port = process.env.PORT || 9999,
bodyParser = require('body-parser'),
mongoose = require('mongoose'),
user = require ('./api/models/userModel');
event = require ('./api/models/eventModel');
event = require ('./api/models/messageModel');

console.log('Creating Database Connection');
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://nalbayrak:ozamanrenkdans@ds141450.mlab.com:41450/gezentidb');
console.log('Database Connected');

app.use(session({secret: 'zalimcoding2017'}));


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


var routes = require('./api/routes/defaultRoutes');
routes(app);

app.listen(port);

console.log('gezenti RESTful API server started on: ' + port);
