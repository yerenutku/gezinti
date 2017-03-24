'use strict';
var mongoose = require('mongoose');
var schema = mongoose.Schema;

var userSchema = new Schema({
  name:{
    type: String,
    Required: 'Kullancı adı giriniz'
  },
  createDate:{
    type: Date,
    default: Date.now
  },
  password:{
    type: String,
    Required: 'Şifre Giriniz'
  }
});

module.exports = mongoose.model('users', userSchema);
