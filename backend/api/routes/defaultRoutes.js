'use strict';
module.exports = function(app){
  var indexController = require('../controllers/indexController');
  var eventController = require('../controllers/eventController');

  app.route('/api/user')
    .post(indexController.selectUser)
    .put(indexController.registerUser)
    .get(indexController.getUsers);

  app.route('/api/event')
    .put(eventController.registerEvent)
    .get(eventController.getEventsByLatLong);

  app.route('/api/event/:eventId')
    .put(eventController.joinEvent)
    .get(eventController.getEventById);
}
