'use strict';
module.exports = function(app){
  var indexController = require('../controllers/indexController');

  app.route('/user')
    .post(indexController.selectUser)
    .put(indexController.registerUser)
    .get(indexController.getUsers);

}
