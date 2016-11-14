window.onload = function () {
    var db = firebase.database().ref(auth.FIREBASE_REF_ADMIN);
    document.getElementById('file').addEventListener('change', handleFileSelect, false);

    db.on('child_added', function (snapshot) {
        var scope = angular.element(document.getElementById("adminCtrl")).scope();
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.arrayPush(snapshot);
            });
        } else {
            angular.element(document.getElementById('adminCtrl')).scope().arrayPush(snapshot);
        }

    });

    db.on('child_removed', function (snapshot) {
        var scope = angular.element(document.getElementById("adminCtrl")).scope();
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.arrayRemove(snapshot.key);
            });
        } else {
            angular.element(document.getElementById('adminCtrl')).scope().arrayRemove(snapshot.key);
        }

    });

    db.on('child_changed', function (snapshot) {
        var scope = angular.element(document.getElementById("adminCtrl")).scope();
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.arrayChange(snapshot);
            });
        } else {
            angular.element(document.getElementById('adminCtrl')).scope().arrayChange(snapshot);
        }

    });
};

function handleFileSelect(evt) {
    evt.stopPropagation();
    evt.preventDefault();
    var storageRef = firebase.storage().ref();
    var file = evt.target.files[0];
    var metadata = {
        'contentType': file.type
    };
    storageRef.child('images/' + localStorage.getItem(auth.LOCAL_STORAGE_ADMIN) + '/' + file.name).put(file, metadata).then(function (snapshot) {
        var url = snapshot.metadata.downloadURLs[0];
        document.getElementById('file_input_text').value = url;
        var scope = angular.element(document.getElementById("adminCtrl")).scope();
        if (!scope.$$phase) {
            scope.$apply(function () {
                scope.setPhotoProperty(url);
            });
        } else {
            angular.element(document.getElementById('adminCtrl')).scope().setPhotoProperty(url);
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
        .module('EasyGuide', ['angular-md5','ngMaterial', 'ngMessages', 'material.svgAssetsCache'])
        .controller('adminCtrl', function ($scope) {
            $scope.admins = Array();

            $scope.arrayPush = function (snapshot) {
                $scope.admin = snapshot.val();
                $scope.admin.key = snapshot.key;
                $scope.admins.push($scope.admin);
            };

            $scope.arrayChange = function (snapshot) {
                $.each($scope.admins, function () {
                    if (this.key == snapshot.key) {
                        this.name = snapshot.val().name;
                        this.email = snapshot.val().email;
                        this.password = snapshot.val().password;
                        this.image_profile = snapshot.val().image_profile;
                    }
                });
            };

            $scope.arrayRemove = function (key) {
                $.each($scope.admins, function (i) {
                    if ($scope.admins[i].key === key) {
                        $scope.admins.splice(i, 1);
                        return false;
                    }
                });
            };

            $scope.save = function () {
                if (validate.validateAdminForm($scope.user)) {
                    if ($scope.user.key != null) {
                        var key = $scope.user.key;
                        delete $scope.user.key;
                        admin.updateAdmin(key, $scope.user);
                    } else {
                        admin.addAdmin($scope.user);
                    }
                    this.resetAdminOnForm();
                }
            };

            $scope.delete = function (key) {
                admin.delAdmin(key);
            };

            $scope.loadAdminOnForm = function (admin) {
                $scope.user = {
                    key: admin.key,
                    email: admin.email,
                    name: admin.name,
                    password: admin.password,
                    image_profile: admin.image_profile
                };
            };

            $scope.resetAdminOnForm = function () {
                $scope.user = {
                    email: '',
                    name: '',
                    password: '',
                    image_profile: ''
                };
            };

            $scope.setPhotoProperty = function (photo) {
                $scope.user.image_profile = photo;
            };
        });

var validate = new function () {
    /**
     * Validate the admin form before do the firebase connection
     * @returns {_L1.ValidateLoginForm.stat|Boolean}
     */
    this.validateAdminForm = function (admin) {
        var stat = true;
        var errorMsg = [];

        if (admin != null) {
            if (admin.email == null || admin.email.trim().length < 3 || !this.validateEmail(admin.email)) {
                stat = false;
                errorMsg.push("Invalid email account");
            }
            if (admin.password == null || admin.password.trim().length < 3) {
                stat = false;
                errorMsg.push("Password should has at least 3 caracteres");
            }
            if (admin.name == null || admin.name.trim().length < 3) {
                stat = false;
                errorMsg.push("Name should has at least 3 caracteres");
            }
        } else {
            stat = false;
            errorMsg.push("All fields is obligatory");
        }
        if (errorMsg.length > 0) {
            var error = '';
            for (var i = 0; i <= 2; i++) {
                if (errorMsg[i] !== undefined) {
                    error += '- ' + errorMsg[i] + '<br />';
                }
            }
            toastr.error(error);
        }


        return stat;
    };

    this.validateEmail = function (email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
    };
};