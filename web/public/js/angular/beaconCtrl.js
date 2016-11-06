/* global beaconModel, toastr, firebase */

window.onload = function () {
    var db = firebase.database().ref('beacon');
    var scope = angular.element(document.getElementById("beaconCtrl")).scope();

    db.on('child_added', function (snapshot) {
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.arrayPush(validate.filterObject(snapshot));
            });
        } else {
            scope.arrayPush(validate.filterObject(snapshot));
        }
    });

    db.on('child_removed', function (snapshot) {
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.arraySplice(validate.filterObject(snapshot));
            });
        } else {
            scope.arraySplice(validate.filterObject(snapshot));
        }

    });

    db.on('child_changed', function (snapshot) {
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.arrayChange(validate.filterObject(snapshot));
            });
        } else {
            scope.arrayChange(validate.filterObject(snapshot));
        }

    });
};

function handleFileSelect(evt) {
    evt.stopPropagation();
    evt.preventDefault();
    var storageRef = firebase.storage().ref();
    var file = evt.target.files[0];
    var scope = angular.element(document.getElementById("beaconCtrl")).scope();
    var metadata = {
        'contentType': file.type
    };
    storageRef.child('images/beacon/content/' + file.name).put(file, metadata).then(function (snapshot) {
        var url = snapshot.metadata.downloadURLs[0];
        document.getElementById('file_input_text').value = url;
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.setPhotoContent(url);
            });
        } else {
            scope.setPhotoContent(url);
        }
    }).catch(function (error) {
        console.error('Upload failed:', error);
    });
}

/**
 * This angular control the form of admins
 * @param {type} param1
 * @param {type} param2
 */
angular
        .module('EasyGuide', ['ngAnimate', 'ngMaterial', 'ngMessages', 'material.svgAssetsCache'])
        .controller('beaconCtrl', function ($scope) {

            $scope.form = Array();
            $scope.beacons = Array();

            $scope.arrayPush = function (object) {
                $scope.beacons.push(object);
            };

            $scope.arrayChange = function (object) {
                $.each($scope.beacons, function () {
                    if (this.key == object.key) {
                        this.uuid = object.uuid;
                        this.location = object.location;
                        this.major_value = object.major_value;
                        this.minor_value = object.minor_value;
                        this.action = object.action;
                        if ($scope.form.beacon.key = this.key) {
                            $scope.form.beacon = angular.copy(object);
                        }
                    }
                });
            };

            $scope.arraySplice = function (object) {
                $.each($scope.beacons, function (i) {
                    if ($scope.beacons[i].key == object.key) {
                        $scope.beacons.splice(i, 1);
                        return false;
                    }
                });
            };

            $scope.set = function (object) {
                if (validate.validateMainForm(object)) {
                    if (object.key != null) {
                        beaconModel.updBeacon(object);
                    } else {
                        beaconModel.setBeacon(object);
                    }
                    this.resetBeaconForm();
                }
            };

            $scope.del = function (object) {
                beaconModel.delBeacon(object);
            };

            $scope.setContent = function (beaconKey, content) {
                if (validate.validateContentForm(content)) {
                    if (content.key != null) {
                        beaconModel.updContent(beaconKey, content);
                    } else {
                        beaconModel.setContent(beaconKey, content);
                    }
                    this.resetContentForm();
                }
            };

            $scope.setPhotoContent = function (photo) {
                if (!$scope.form.content) {
                    $scope.form.content = Array();
                }
                $scope.form.content.image_url = photo;
            };

            $scope.delContent = function (beaconKey, content) {
                beaconModel.delContent(beaconKey, content);
            };

            $scope.loadBeaconOnForm = function (beacon) {
                $scope.form.beacon = angular.copy(beacon);
                $scope.form.content = angular.copy(beacon.content);
            };

            $scope.loadContentOnForm = function (content) {
                $scope.form.content = angular.copy(content);
            };

            $scope.resetBeaconForm = function () {
                $scope.form.beacon = Array();
                $scope.form.content = Array();
            };

            $scope.resetContentForm = function () {
                $scope.form.content = Array();
            };

            $scope.addChangeListener = function () {
                var file = document.getElementById('file');
                if (file) {
                    $("file").unbind("change");
                    file.addEventListener('change', handleFileSelect, false);
                }
            };
        });

var validate = new function () {
    /**
     * Validate the beacon form before do the firebase connection
     * @returns {_L1.ValidateLoginForm.stat|Boolean}
     */
    this.validateMainForm = function (object) {
        var stat = true;
        var errorMsg = [];

        if (object != null) {
            if (object.uuid == null || object.uuid.length < 3) {
                stat = false;
                errorMsg.push("UUID should has at least 3 numbers");
            }
            if (object.location == null || object.location.length < 3) {
                stat = false;
                errorMsg.push("Location should has at least 3 caracteres");
            }
            if (object.major_value == null || object.major_value.length < 3) {
                stat = false;
                errorMsg.push("Mayor Value should has at least 3 caracteres");
            }
            if (object.minor_value == null || object.minor_value.length < 3) {
                stat = false;
                errorMsg.push("Minor Value should has at least 3 caracteres");
            }
        } else {
            stat = false;
            errorMsg.push("All fields is obligatory");
        }
        if (errorMsg.length > 0) {
            formatAndShowToast(errorMsg);
        }


        return stat;
    };

    this.validateContentForm = function (object) {
        var stat = true;
        var errorMsg = [];
        if (object != null) {
            if (object.profile == null || object.profile.length == 0) {
                stat = false;
                errorMsg.push("Profile should be filled");
            }
            if (object.speech_content == null || object.speech_content.length < 3) {
                stat = false;
                errorMsg.push("Speech content should has at least 3 caracteres");
            }
            if (object.speech_description == null || object.speech_description.length < 3) {
                stat = false;
                errorMsg.push("Speech description should has at least 3 caracteres");
            }
            if (object.text_content == null || object.text_content.length < 3) {
                stat = false;
                errorMsg.push("Text content should has at least 3 caracteres");
            }
            if (object.text_description == null || object.text_description.length < 3) {
                stat = false;
                errorMsg.push("Text description should has at least 3 caracteres");
            }
        } else {
            stat = false;
            errorMsg.push("All fields is obligatory");
        }
        if (errorMsg.length > 0) {
            formatAndShowToast(errorMsg);
        }


        return stat;
    };

    this.filterObject = function (snapshot) {
        var beacon = snapshot.val();
        beacon.key = snapshot.key;
        for (var key in beacon.content) {
            beacon.content[key].key = key;
        }
        return beacon;
    };
};

var formatAndShowToast = function (msg) {
    var error = '';
    for (var i = 0; i <= 2; i++) {
        if (msg[i] !== undefined) {
            error += '- ' + msg[i] + '<br />';
        }
    }
    toastr.error(error);
};