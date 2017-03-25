'use strict';

var mongoose = require('mongoose'),
events = mongoose.model('events'),
users = mongoose.model('users'),
express = require('express'),
session = require('express-session');
var sess;

exports.joinEvent = function(req,res){
  var userToAdd;
  users.findOne({_id: req.body.userId}, function(err,currentUser){
    if(err)
      res.send(err);
    userToAdd = currentUser;
  });
  console.log(req.params.eventId);
  events.findOne({_id: req.params.eventId}, function(err, currentEvent){
    if(err)
      res.send(err);
    /*if(userToAdd._id === currentEvent._id) // user cant join his own event
      res.send('user is owner');*/
    currentEvent.members.push(userToAdd);
    currentEvent.save(function(err,savedEvent){
      if(err)
        res.send(err);
      res.json(savedEvent);
    });
  });
};

exports.getEventById = function(req,res){

};

exports.getEventsByLatLong = function(req,res){
  var lat = req.body.lat, long = req.body.long, radius = 30;
  events.find({}, function(err, eventList) {
    if(err)
      res.send(err);
      var searchOpts = {spherical: true,maxDistance: radius*1000,num: 30}; // convert km to meters
      var point = {type:'Point', coordinates: [lat, long] };
      events.geoNear(point,searchOpts,function(err, results){
        if(err)
          res.send(err);
        res.json(matchList);;
      });
  });
};

exports.registerEvent = function(req,res){
  var newEvent = new events(req.body);
  console.log(req.body);
  newEvent.save(function(err,savedEvent){
    if(err)
      res.send(err);
    res.json(savedEvent);
  });
};
