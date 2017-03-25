'use strict';

var mongoose = require('mongoose'),
events = mongoose.model('events'),
users = mongoose.model('users'),
express = require('express'),
session = require('express-session');
var sess;

// adds user to event
exports.joinEvent = function(req,res){
  var userToAdd;
  users.findOne({_id: req.body.userId}).then(function(currentUser){
    userToAdd = currentUser;
  }).catch(console.error);
  events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
    /*if(userToAdd._id === currentEvent._id) // user cant join his own event
      res.send('user is owner');*/
      console.log(currentEvent);
      console.log(userToAdd);
    currentEvent.members.push(userToAdd);
    currentEvent.save().exec().then(function(savedEvent){
      res.json(savedEvent);
    });
  }).catch(console.error);
};

exports.getEventById = function(req,res){
  events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
    res.json(currentEvent);
  });
};

exports.getEventsByLatLong = function(req,res){
  var lat = req.body.lat, long = req.body.long, radius = 15;
  events.find({}).exec().then(function(eventList) {
    var searchOpts = {spherical: true,maxDistance: radius*1000 ,num: 30}; // convert km to meters
    var point = {type:'Point', coordinates: [lat, long] };
    events.geoNear(point,searchOpts).exec().then(function(){
      res.json(matchList);
    });
  }).catch(console.error);
};

exports.registerEvent = function(req,res){
  var newEvent = new events(req.body);
  newEvent.save().exec().then(function(savedEvent){
    res.json(savedEvent);
  }).catch(console.error);
};
