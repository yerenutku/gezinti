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
  lat:{
    type: String,
    required: 'Address lat is required'
  },
  long:{
    type: String,
    required: 'Address long is required'
  },
  owner:{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'users',
    Required: 'User is required',
  },
  members:{
    type: Array,
    default: []
  }
});

module.exports = mongoose.model('events', eventSchema);
