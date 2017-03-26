'use strict';
var mongoose = require('mongoose');
var schema = mongoose.Schema;

var userSchema = new schema({
  name:{
    type: String,
    Required: 'Kullancı adı giriniz'
  },
  createDate:{
    type: Date,
    default: Date.now
  },
  tel:{
    type:String
  },
  password:{
    type: String,
    Required: 'Şifre Giriniz'
  }
});

module.exports = mongoose.model('users', userSchema);
