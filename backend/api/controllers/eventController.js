'use strict';

var mongoose = require('mongoose'),
events = mongoose.model('events'),
users = mongoose.model('users'),
comments = mongoose.model('comments'),
express = require('express'),
session = require('express-session');
var sess;

// adds user to event
exports.joinEvent = function(req,res){
  events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
    if(req.params.userId == currentEvent.owner){
      res.status(400);
      res.send('user is owner');
    } // user cant join his own event
    console.log(currentEvent.members);
    console.log(req.params.userId);
    if(currentEvent.members.indexOf(""+req.params.userId) > 0){
      res.status(400);
      res.send('user already joined');
    }
    if(new Date(currentEvent.time)<new Date()){
      res.status(400);
      res.send('passed event');
    }
    currentEvent.members.push(req.params.userId);
    currentEvent.save().then(function(savedEvent){
      res.json(savedEvent);
    }).catch(console.error);
  }).catch(console.error);
};

exports.removeUserFromEvent = function(req,res){
    events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
      if(currentUser._id === currentEvent._id) // user cant join his own event
        res.send('user is owner');
      if(Date(currentEvent.time)<new Date())
        res.send('passed event')
      currentEvent.members.splice(req.params.userId,1);
      currentEvent.save().then(function(savedEvent){
        res.json(savedEvent);
      }).catch(console.error);
    }).catch(console.error);
};

exports.getEventById = function(req,res){
  events.findOne({_id: req.params.eventId}).populate('owner').exec().then(function(currentEvent){
    res.json(currentEvent);
  }).catch(console.error);
};

exports.getEventsByLatLong = function(req,res){
  var lat = req.body.lat, long = req.body.long, radius = 15;
  var searchOpts = {spherical: true,maxDistance: radius*1000 ,num: 30}; // convert km to meters
  var point = {
    type: "Point",
    coordinates: [lat,long]
  };
  events.geoNear(point,searchOpts).then(function(){
    res.json(matchList);
  }).catch(console.error);
};

exports.registerEvent = function(req,res){
  var newEvent = new events(req.body);
  if(Date(newEvent.time)<new Date())
    res.send('cannot create event with passed date');
  newEvent.save().then(function(savedEvent){
    res.json(savedEvent);
  }).catch(console.error);
};

exports.removeEvent = function(req,res){
  events.findOne({_id: req.params.eventId}).remove().exec().then(function(){
    res.send('deleted');
  }).catch(console.error);
};

exports.commentEvent = function(req,res){
  var newComment = new comments(req.body);
  newComment.save().then(function(savedComment){
    events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
      currentEvent.comments.push(savedComment);
      currentEvent.save().then(function(savedEvent){
        res.send(savedEvent)
      }).catch(console.error);
    }).catch(console.error);
  }).catch(console.error);
};

exports.removeComment = function(req,res){
  comments.findOne({_id: req.body.commentId}).exec().then(function(currentComment){
    events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
      currentEvent.comments.splice(currentEvent.comments.indexOf(currentComment),1);
      currentEvent.save().then(function(savedEvent){
        res.send(savedEvent)
      }).catch(console.error);
    }).catch(console.error);
  }).catch(console.error);
};
