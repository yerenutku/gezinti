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
      throw(400);
    } // user cant join his own event
    if(currentEvent.members.indexOf(req.params.userId) > -1){
      res.status(400);
      res.send('user already joined');
      throw(400);
    }
    if(new Date(currentEvent.time)<new Date()){
      res.status(400);
      res.send('passed event');
      throw(400);
    }
    currentEvent.members.push(req.params.userId);
    currentEvent.save().then(function(savedEvent){
      res.json(savedEvent);
    }).catch(console.error);
  }).catch(console.error);
};

exports.removeUserFromEvent = function(req,res){
    events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
      if(req.params.userId == currentEvent.owner) // user cant join his own event
        res.send('user is owner');
      if(new Date(currentEvent.time)<new Date())
        res.send('passed event')
      var index = currentEvent.members.indexOf(req.params.userId);
      console.log(index);
      if(index>-1)
        currentEvent.members.splice(index,1);
      currentEvent.save().then(function(savedEvent){
        res.json(savedEvent);
      }).catch(console.error);
    }).catch(console.error);
};

exports.getEventById = function(req,res){
  events.findOne({_id: req.params.eventId}).populate('owner').populate('members').exec().then(function(currentEvent){
    res.json(currentEvent);
  }).catch(console.error);
};

if(typeof(Number.prototype.toRad) === "undefined") {
    Number.prototype.toRad = function () {
        return this * Math.PI / 180;
    }
}
function getDistance(point1,point2){
  var decimals = 6;
  var earthRadius = 6371; // km
  var lat1 = parseFloat(point1.lat), lat2 = parseFloat(point2.lat), lon1 = parseFloat(point1.lon), lon2 = parseFloat(point2.lon);
  var dLat = (lat2 - lat1).toRad();
  var dLon = (lon2 - lon1).toRad();
  var lat1 = lat1.toRad();
  var lat2 = lat2.toRad();

  var a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
           Math.sin(dLon / 2) * Math.sin(dLon / 2) * Math.cos(lat1) * Math.cos(lat2);
  var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  var d = earthRadius * c;
  return Math.round(d * Math.pow(10, decimals)) / Math.pow(10, decimals);
}

exports.getEventsByLatLong = function(req,res){
  events.find({}).populate('owner').populate('members').exec().then(function(eventList){
    var resultList = {}, lat1 = req.body.lat, lon1 = req.body.lon, resultArray = [];
    eventList.forEach(function(elem){
      var locations = elem.location;
      for(var i = 0; i < locations.length; i++){
        var pair = locations[i];
        var distance = getDistance(pair,{lat:lat1,lon:lon1});
        if(distance*2 < 10){
          console.log(distance);
          resultArray.push(elem);
          break;
        }
      }
    });
    resultList.result = resultArray;
    res.json(resultList);
  }).catch(function(err){
    console.log('hata',err);
    res.status(500).send({error: err});
  });
};

exports.registerEvent = function(req,res){
  try{
    var newEvent = new events(req.body);
  }catch(err){
    console.log('cathed error');
    res.status(405).send({error: err});
  }
  if(new Date(newEvent.time)<new Date())
    res.send('cannot create event with passed date');
  newEvent.save().then(function(savedEvent){
    savedEvent.populate('owner',function(err,populatedEvent){
        if(err)
          res.send(err);
        res.json(populatedEvent);
    });
  }).catch(function(err){
    console.log('hata',err);
    res.status(500).send({error: err});
  });
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
      currentEvent.comments.push(savedComment._id);
      currentEvent.save().then(function(savedEvent){
        res.send(savedEvent)
      }).catch(console.error,res.send({status: res.status(), data: error}));
    }).catch(console.error,res.send({status: res.status(), data: error}));
  }).catch(console.error,res.send({status: res.status(), data: error}));
};

exports.removeComment = function(req,res){
    events.findOne({_id: req.params.eventId}).exec().then(function(currentEvent){
      currentEvent.comments.splice(currentEvent.comments.indexOf(req.body.commentId),1);
      currentEvent.save().then(function(savedEvent){
        res.send(savedEvent)
      }).catch(console.error);
    }).catch(console.error);
};
