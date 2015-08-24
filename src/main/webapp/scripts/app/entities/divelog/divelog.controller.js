'use strict';

angular.module('ozayApp')
.controller('DiveLogController', function ($rootScope, $scope, $cookieStore, Dashboard) {
    $scope.dashboard = [];

		$scope.sites = sites;

		var sites = [
          "Shaab El Erg",
          "Abu Gotta Ramada",
          "El Arouk",
          "Small Giftun",
          "Erg Somaya"
        ];

});
