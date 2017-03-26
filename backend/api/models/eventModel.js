'use strict';
var mongoose = require('mongoose');
var schema = mongoose.Schema;

var eventSchema = new schema({
  title:{
    type: String,
    Required: 'Title is required'
  },
  createDate:{
    type: Date,
    default: Date.now
  },
  desc:{
    type: String,
    required: 'Description is required'
  },
  time:{
    type: Date,
    required: 'Time is required'
  },
  owner:{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'users',
    Required: 'User is required',
  },
  members:[{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'users'
  }],
  location:[{
    lat : String,
    lon : String,
    _id: false
  }],
  eventType:{
    type: Number,
    default: 0,
    Required: 'Event type is required'
  },
  eventTime:{
    type: Number,
    Required: 'Event time is required'
  }
});

module.exports = mongoose.model('events', eventSchema);
