var mainModel = angular.module('mainApp',['ngMap']);

mainModel.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);


mainModel.controller('indexController',function indexController($scope,$http,NgMap) {
  var currentUser = "58d6bdc889f42f0544a8721e";
  $scope.checkCurrentUser = false;
  $scope.pageIndex = 0;
  $scope.currentEvent = {};
  var apiBase = "http://192.168.88.63:9999";
  var vm = this;
  NgMap.getMap().then(function(map) {
    console.log('map', map);
    vm.map = map;
  });

  vm.clicked = function() {
    vm.getEvent(vm.shop._id);
  };

  vm.showDetail = function(e, shop) {
    vm.shop = shop;
    vm.map.showInfoWindow('foo-iw', shop._id);
  };

  vm.hideDetail = function() {
    vm.map.hideInfoWindow('foo-iw');
  };

  function initMap() {
    // Try HTML5 geolocation.
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(position) {
        $scope.mapConf = { center: [position.coords.latitude, position.coords.longitude], zoom: 13 };
        //get default search data
        var req = {
          method: 'POST',
          url: apiBase + "/api/event/search",
          headers: {
            'Content-Type': "application/json"
          },
          dataType: "jsonp",
          data: {lat:position.coords.latitude, lon:position.coords.longitude, eventTime: 0, eventType: 0}
        };


        $http(req).then(function (response) {
          console.log(response);
          var curCount = 0;
          var events = response.data.result;
          $scope.markers = [];
          for(var i = 0; i < events.length; i++){
            var locs = events[i].location;
            for(var j = 0; j < locs.length; j++){
              $scope.markers.push({loc:[locs[j].lat, locs[j].lon], title:events[i].title, _id: events[i]._id, count: curCount});
              curCount++;
            }
          }
          vm.markers = $scope.markers;
        }).catch(function(error){
          console.log(error);
        });
      });
    }
  };
  vm.getEvent = function(eventId){
    var req = {
      method: 'GET',
      url: apiBase + "/api/event/"+eventId,
      headers: {
        'Content-Type': "application/json"
      },
      dataType: "jsonp"
    };
    $http(req).then(function (response) {
      $scope.pageIndex = 1;
      $scope.currentEvent = response.data;
      if($scope.currentEvent.owner._id == currentUser)
        $scope.checkCurrentUser = true;
      else
        $scope.checkCurrentUser = false
      var isCurrentJoined = $scope.currentEvent.members.filter(function(value){
        if(value._id == currentUser)
          return value;
      });
      if(isCurrentJoined.length > 0)
        $scope.userJoined = true;
      else
        $scope.userJoined = false;
    }).catch(function(error){
      console.log(error);
    });
  }

  $scope.join = function(){
      var req = {
        method: 'GET',
        url: apiBase + "/api/event/"+$scope.currentEvent._id+"/"+currentUser+"/join",
        headers: {
          'Content-Type': "application/json"
        },
        dataType: "jsonp"
      };
      $http(req).then(function (response) {
        $scope.pageIndex = 1;
        console.log(response);
        vm.getEvent($scope.currentEvent._id);
      }).catch(function(error){
        console.log(error);
      });
  };

    $scope.cancel = function(){
        var req = {
          method: 'GET',
          url: apiBase + "/api/event/"+$scope.currentEvent._id+"/"+currentUser+"/remove",
          headers: {
            'Content-Type': "application/json"
          },
          dataType: "jsonp"
        };
        $http(req).then(function (response) {
          $scope.pageIndex = 1;
          console.log(response);
          vm.getEvent($scope.currentEvent._id);
          $scope.userJoined = false;
        }).catch(function(error){
          console.log(error);
        });
    };
  initMap();
});
