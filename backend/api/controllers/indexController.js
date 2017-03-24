'use strict';

var mongoose = require('mongoose'),
user = mongoose.model('users'),
express = require('express');
session = require('express-session'),
redisStore = require('connect-redis');

exports.loginUser = function(req,res){
  user.findById(req.params.userId, function(err, logUser){
    if(err || typeOf(logUser) = undefined)
      res.send(err);
    if(logUser.password = req.password)
      res.send User
  });
};
