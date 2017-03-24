'use strict';

var mongoose = require('mongoose'),
user = mongoose.model('users'),
express = require('express'),
session = require('express-session');
var sess;
exports.getUsers = function(req,res){
  user.find({},{_id: 1, name: 1, password: 1}, function(err, users) {
    if(err)
      res.send(err);
    res.json(users);
  });
};

exports.registerUser = function(req,res){
  var newUser = new user(req.body);
  newUser.save(function(err,savedUser){
    if(err)
      res.send(err);
    res.json(savedUser);
  });
};


exports.selectUser = function(req,res){
  if(typeof(req.body.userId) == undefined)
    res.send("userId is required");
  user.findOne({_id: req.body.userId}, function(err,currentUser){
    sess = req.session;
    sess.user = currentUser;
    res.json(currentUser);
  });
};
