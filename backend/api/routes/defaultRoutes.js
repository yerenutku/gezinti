'use strict';
module.exports = function(app){
  var indexController = require('../controllers/indexController');
  var eventController = require('../controllers/eventController');

  app.route('/api/user/:userId/select')
    .post(indexController.selectUser)
  app.route('/api/user/register')
    .post(indexController.registerUser);
  app.route('api/user')
    .get(indexController.getUsers);
  app.route('/api/event/register')
    .post(eventController.registerEvent);
  app.route('api/event/search')
    .post(eventController.getEventsByLatLong);
  app.route('/api/event/:eventId')
    .get(eventController.getEventById);
  app.route('/api/event/:eventId/comment')
    .post(eventController.commentEvent);
  app.route('/api/event/:eventId/removeComment')
    .post(eventController.removeComment);
  app.route('/api/event/:eventId/remove')
    .post(eventController.removeEvent);
  app.route('/api/event/:eventId/:userId/join')
    .post(eventController.joinEvent);
  app.route('/api/event/:eventId/:userId/remove')
    .post(eventController.removeUserFromEvent);
}
