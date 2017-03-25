'use strict';
module.exports = function(app){
  var indexController = require('../controllers/indexController');
  var eventController = require('../controllers/eventController');

  app.route('/api/user')
    .post(indexController.selectUser)
    .put(indexController.registerUser)
    .get(indexController.getUsers);

  app.route('/api/event/register')
    .post(eventController.registerEvent);
  app.route('api/event/serach')
    .post(eventController.getEventsByLatLong);

  app.route('/api/event/:eventId')
    .get(eventController.getEventById)
    .delete(eventController.removeEvent);

  app.route('/api/event/:eventId/:userId')
    .put(eventController.joinEvent)
    .delete(eventController.removeUserFromEvent);
}
