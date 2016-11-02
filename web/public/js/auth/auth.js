/* global admin, log, toastr, firebase */

var auth = new function () {

    this.WRONG_PASS = 'The password you entered is incorrect';
    this.USER_NOT_FOUND = 'The e-mail you entered does not match any account';
    this.AUTHENTICATION = 'authentication';
    this.AUTHENTICATION_SUCCESS = 'authentication_success';
    this.FIREBASE_REF_LOG = 'log';
    this.FIREBASE_REF_ADMIN = 'admin';
    this.FIREBASE_REF_WEB = 'web';
    this.LOCAL_STORAGE_ADMIN = 'admin';
    this.INCORRECT_EMAIL = 'Username must have more than 2 characters!';
    this.INCORRECT_PASS = 'Password must have more than 2 characters!';

    this.unauth = function () {
        localStorage.removeItem('user');
    };

    this.firebaseAuthByUserAndPass = function () {
        if (this.ValidateLoginForm()) {
            firebase
                    .database()
                    .ref('/admin')
                    .orderByChild('email')
                    .startAt($('#username').val())
                    .endAt($('#username').val())
                    .on('value', function (snapshot) {
                        var stat = false;
                        var userFound = false;
                        if (snapshot.val()){
                            $.each(snapshot.val(), function (index, values) {
                                userFound = true;
                                if (values.password == $('#password').val()) {
                                    admin.storeAdmin(index, values);
                                    window.location = 'index.html';
                                }
                            });
                        }
                        if (!stat) {
                            var throwable = userFound ? auth.WRONG_PASS : auth.USER_NOT_FOUND;
                            toastr.error(throwable);
                            log.setLog(null, auth.FIREBASE_REF_LOG + "/" + auth.FIREBASE_REF_WEB, {
                                description: auth.AUTHENTICATION,
                                content: {
                                    status: false,
                                    content_description: throwable,
                                    attempt: {
                                        username: $('#username').val(),
                                        password: $('#password').val()
                                    }
                                }
                            });
                        }
                    });
        }
    };

    /**
     * Verify if user has access yet
     * @returns {undefined}
     */
    this.firebaseAuthByHash = function () {
        firebase
                .database()
                .ref('/admin/' + hash)
                .on('value', function (snapshot) {
                    var stat = false;
                    if (snapshot.val())
                        $.each(snapshot.val(), function (index, values) {
                            if (values.last_access > (firebase.database.ServerValue.TIMESTAMP - 86400000)) {
                                window.location = 'login.html';
                            } else {
                                window.location = 'dashboard.html';
                            }
                        });
                    else {
                        window.location = 'login.html';
                    }
                });
    };

    /**
     * Validate the login form before do the firebase connection
     * @returns {_L1.ValidateLoginForm.stat|Boolean}
     */
    this.ValidateLoginForm = function () {
        var stat = true;
        var errorMsg = [];

        if ($('#username').val() == null || $('#username').val().trim().length < 3) {
            stat = false;
            errorMsg.push(auth.INCORRECT_EMAIL);
        }
        if ($('#password').val() == null || $('#password').val().trim().length < 3) {
            stat = false;
            errorMsg.push(auth.INCORRECT_PASS);
        }
        if (errorMsg.length > 0) {
            var error = '';
            for (var i = 0; i <= 2; i++) {
                if (errorMsg[i] !== undefined) {
                    error += '- ' + errorMsg[i] + '<br />';
                }
            }
            toastr.error(error);
            log.setLog(null, auth.FIREBASE_REF_LOG + "/" + auth.FIREBASE_REF_WEB, {
                description: auth.AUTHENTICATION,
                content: {
                    status: false,
                    content_description: error.replace('<br />', ''),
                    attempt: {
                        username: $('#username').val(),
                        password: $('#password').val()
                    }
                }
            });
        }

        return stat;
    };
};