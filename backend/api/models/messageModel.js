'use strict';
var mongoose = require('mongoose');
var schema = mongoose.Schema;

var eventSchema = new schema({
  sender:{
    type: mongoose.Schema.Types.ObjectId,
    ref: 'users',
    required: 'sender id is required'
  },
  createDate:{
    type: Date,
    default: Date.now
  },
  message:{
    type: String,
    Required: 'message is required',
  }
});

module.exports = mongoose.model('comments', eventSchema);
