'use strict';

angular.module('ozayApp')
.controller('DiveLogController', function ($rootScope, $scope, $cookieStore, Dashboard) {
    $scope.divelog = [];

	$rootScope.$watch('selectedBuilding', function(){
		if($rootScope.selectedBuilding !== undefined){
			var buildingId = $rootScope.selectedBuilding;
			Dashboard.get({building:buildingId},function(data) {
                $scope.divelog = data;
			});
		}
	});




});



