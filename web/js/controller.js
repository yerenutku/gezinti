var mainModel = angular.module('mainApp',['ngMap']);

mainModel.config(['$qProvider', function ($qProvider) {
    $qProvider.errorOnUnhandledRejections(false);
}]);


mainModel.controller('indexController',function indexController($scope,$http,NgMap) {
  var currentUser = "58d6bdc889f42f0544a8721e";
  $scope.checkCurrentUser = false;
  $scope.pageIndex = 0;
  $scope.createMode = 0;
  $scope.currentEvent = {};
  $scope.typeSelects = [
    {tip:"Seçiniz", value: 0},
    {tip:"Yeme", value: 1},
    {tip:"İçme", value : 2},
    {tip:"Sinema", value : 3},
    {tip:"Gezi", value : 4},
  ];
  $scope.typeSelected = $scope.typeSelects[0];
  $scope.timeSelects = [
    {tip:"Seçiniz", value : 0},
    {tip:"Sabah", value : 1},
    {tip:"Öğlen", value : 2},
    {tip:"Akşam", value : 3},
    {tip:"Gece", value : 4},
  ];
  $scope.timeSelected = $scope.timeSelects[0];
  var apiBase = "http://192.168.88.63:9999";
  var vm = this;
  NgMap.getMap().then(function(map) {
    console.log('map', map);
    vm.map = map;
  });

  vm.clicked = function(event) {
    if($scope.createMode == 0)
      vm.getEvent(vm.shop._id);
    else
      vm.placeMarker(event);
  };
  vm.placeMarker = function(e) {
    if($scope.createMode == 0)
      return;
    console.log("entered place marker");
    console.log(e.latLng.lat());
    vm.markers.push({loc:[e.latLng.lat(),e.latLng.lng()]});
    vm.map.panTo(e.latLng);
  }
  vm.showDetail = function(e, shop) {
    vm.shop = shop;
    vm.map.showInfoWindow('foo-iw', shop._id);
  };

  vm.hideDetail = function(position) {
    vm.map.hideInfoWindow('foo-iw');
  };

  function searchByPoint(data){

    var req = {
      method: 'POST',
      url: apiBase + "/api/event/search",
      headers: {
        'Content-Type': "application/json"
      },
      dataType: "jsonp",
      data: data
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
        vm.markers = $scope.markers;
      }
    }).catch(function(error){
        console.log(error);
    });
  }

  function initMap() {
    // Try HTML5 geolocation.
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(function(position) {
        $scope.mapConf = { center: [position.coords.latitude, position.coords.longitude], zoom: 13 };
        data = {lat:position.coords.latitude, lon:position.coords.longitude, eventTime: 0, eventType: 0};
        searchByPoint(data);
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
      vm.markers = [];
      var curCount = 0;
      var locs = $scope.currentEvent.location;
      for(var i = 0; i < locs.length; i++){
        vm.markers.push({loc:[locs[i].lat, locs[i].lon], title:$scope.currentEvent.title, _id: $scope.currentEvent._id, count: curCount});
      }
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

    $scope.showCreate = function(){
      $scope.lastPage = $scope.pageIndex;
      $scope.pageIndex = 2;
      $scope.lastMarkers = vm.markers;
      vm.markers = [];
      $scope.createMode = 1;
    };

    $scope.cancelCreate = function(){
      vm.markers = [];
      $scope.pageIndex = $scope.lastPage;
      vm.markers = $scope.lastMarkers;
      $scope.createMode = 0;
    };

    $scope.saveEvent = function(){
      dataMarkers = []
      for(var i = 0; i < vm.markers; i++){
        var currentMarker = vm.markers[i];
        dataMarkers.push({lat:currentMarker.loc[0], lon: currentMarker.loc[1]});
      }
      var data = {
        title: $scope.title,
        owner: currentUser,
        desc: $scope.desc,
        time: "2017-05-18T16:00:00.000Z",
        eventType: $scope.typeSelected.value,
        eventTime: $scope.timeSelected.value,
        location: dataMarkers
      };
      var req = {
        method: 'POST',
        url: apiBase + "/api/event/register",
        headers: {
          'Content-Type': "application/json"
        },
        data: data,
        dataType: "jsonp"
      };
      $http(req).then(function (response) {
        getEvent(response.data._id);
      }).catch(function(error){
        console.log(error);
      });
    };

    $scope.goDefaultView = function(){
      data = {lat:$scope.mapConf.center[0], lon:$scope.mapConf.center[1], eventTime: 0, eventType: 0};
      searchByPoint(data);
      $scope.pageIndex = 0;
    };
  initMap();
});
