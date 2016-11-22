var app = angular.module("EasyGuide", ["firebase", 'ngSanitize','ngMaterial']);
app.controller("logCtrl", function ($scope, $firebaseArray) {
    var ref = firebase.database().ref().child("log");
    $scope.logs = $firebaseArray(ref.child("user"));
});