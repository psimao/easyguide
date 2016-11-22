/* global firebase */

this.beaconModel = new function () {

    this.beaconRef = 'beacon';
    this.actionRef = 'action';
    this.contentRef = 'content';

    this.setBeacon = function (object) {
        delete object.$priority;
        var db = firebase.database().ref(beaconModel.beaconRef);
        object.dt_created = firebase.database.ServerValue.TIMESTAMP;
        return db.push(object);
    };

    this.updBeacon = function (object) {
        var db = firebase.database().ref(beaconModel.beaconRef + '/' + object.$id);
        object.dt_changed = firebase.database.ServerValue.TIMESTAMP;
        delete object.$id;
        delete object.$priority;
        return db.update(angular.fromJson(angular.toJson(object)));
    };

    this.delBeacon = function (object) {
        delete object.$priority;
        var db = firebase.database().ref(beaconModel.beaconRef);
        return db.child(object.$id).remove();
    };


    this.setContent = function (parentKey, object) {
        delete object.$priority;
        var db = firebase.database().ref(beaconModel.beaconRef + '/' + parentKey + '/' + beaconModel.contentRef);
        object.dt_created = firebase.database.ServerValue.TIMESTAMP;
        return db.push(object);
    };

    this.updContent = function (parentKey, object) {
        var db = firebase.database().ref(beaconModel.beaconRef + '/' + parentKey + '/' + beaconModel.contentRef + '/' + object.$id);
        object.dt_changed = firebase.database.ServerValue.TIMESTAMP;
        delete object.$id;
        delete object.$priority;
        return db.update(angular.fromJson(angular.toJson(object)));
    };

    this.delContent = function (parentKey, object) {
        console.log(object);
        delete object.$priority;
        var db = firebase.database().ref(beaconModel.beaconRef + '/' + parentKey + '/' + beaconModel.contentRef);
        return db.child(object.$id).remove();
    };
};