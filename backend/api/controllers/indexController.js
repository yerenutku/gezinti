'use strict';

//declerations
var mongoose = require('mongoose'),
user = mongoose.model('users'),
express = require('express');

//get all users
exports.getUsers = function(req,res){
  user.find({}).exec().then(function(users) {
    res.json(users);
  }).catch(console.error);
};

//create a new user
//patlıyor mu
exports.registerUser = function(req,res){
  var newUser = new user(req.body);
  newUser.save().then(function(savedUser){
    res.json(savedUser);
  }).catch(console.error);
};

//select a user as active
exports.selectUser = function(req,res){
  user.findOne({_id: req.body.userId}).exec().then(function(){
    res.json(currentUser);
  }).catch(console.error);
};

exports.getUserProfile = function(req,res){
  responseObj = {}
  user.findOne({_id: req.params.userId}).exec().then(function(currentUser){
    responseObj.user = currentUser;
  }).then(function(){
    events.find({members:{_id: req.params.userId}}).exec().then(function(eventList){
      responseObj.events = eventList;
    });
  }).catch(function(err){
    console.log('hata',err);
    res.status(500).send({error: err});
  });
}
