var app = angular.module("EasyGuide", ["firebase"]);
app.controller("logCtrl", function ($scope, $firebaseArray) {
    var ref = firebase.database().ref().child("log");
    $scope.webLog = $firebaseArray(ref.child("web"));
    $scope.userLog = $firebaseArray(ref.child("user"));
});