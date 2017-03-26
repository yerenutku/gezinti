var express = require('express'),
session = require('express-session'),
app = express(),
port = process.env.PORT || 9999,
bodyParser = require('body-parser'),
mongoose = require('mongoose'),
user = require ('./api/models/userModel');
event = require ('./api/models/eventModel');
event = require ('./api/models/messageModel');

var allowCrossDomain = function(req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Methods', 'GET,POST');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization, Content-Length, X-Requested-With');

    // intercept OPTIONS method
    if ('OPTIONS' == req.method) {
      res.send(200);
    }
    else {
      next();
    }
};

console.log('Creating Database Connection');
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://nalbayrak:ozamanrenkdans@ds141450.mlab.com:41450/gezentidb');
console.log('Database Connected');

app.use(session({secret: 'zalimcoding2017'}));


app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(allowCrossDomain);


var routes = require('./api/routes/defaultRoutes');
routes(app);

app.listen(port);

console.log('gezenti RESTful API server started on: ' + port);
