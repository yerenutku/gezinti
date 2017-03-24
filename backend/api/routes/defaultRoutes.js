'use strict';
module.exports = function(app){
  var indexController = require('../controllers/indexController');


  app.route('/user')
    .put(indexController.loginUser);
  app.route('/event')
    .put();
}
