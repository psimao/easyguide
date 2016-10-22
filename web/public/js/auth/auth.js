var auth = new function () {

    this.WRONG_PASS = 'The password you entered is incorrect';
    this.USER_NOT_FOUND = 'The e-mail you entered does not match any account';
    this.AUTHENTICATION = 'authentication';
    this.FIREBASE_REF_LOG = 'log';
    this.FIREBASE_REF_ADMIN = 'admin';
    this.LOCAL_STORAGE_ADMIN = 'admin';
    this.INCORRECT_EMAIL = 'Username must have more than 2 characters!';
    this.INCORRECT_PASS = 'Password must have more than 2 characters!';

    this.unauth = function () {
        localStorage.removeItem('user');
    };

    this.firebaseAuth = function () {
        if (this.validateForm()) {
            firebase
                    .database()
                    .ref('/admin')
                    .orderByChild('email')
                    .startAt($('#username').val())
                    .endAt($('#username').val())
                    .on('value', function (snapshot) {
                        var stat = false;
                        var userFound = false;
                        if (snapshot.val())
                            $.each(snapshot.val(), function (index, values) {
                                userFound = true;
                                if (values.password == $('#password').val()) {
                                    auth.storeAdmin(index, values);
                                    window.location = 'index.html';
                                    stat = true;
                                }
                            });
                        if (!stat) {
                            var throwable = userFound ? auth.WRONG_PASS : auth.USER_NOT_FOUND;
                            toastr.error(throwable);
                            auth.setLog(null, {
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
     * Store the admin on localStorage to keep logged.
     * @param {type} hash user
     * @param {type} fields of current user
     * @returns {undefined} void
     */
    this.storeAdmin = function (hash, fields) {
        localStorage.setItem(auth.LOCAL_STORAGE_ADMIN, JSON.stringify(fields));
        auth.setLog(hash, {
            description: auth.AUTHENTICATION,
            content: {
                keep_logged: true,
                timeout: '',
                content_description: 'Authentication Success',
                status: true
            }
        });
    };

    /**
     * Set the log in 'admin' child.
     * @param {type} hash user
     * @param {type} message object
     * @returns {undefined} void
     */
    this.setLog = function (hash, message) {
        var db = firebase.database().ref(auth.FIREBASE_REF_LOG + '/' + auth.FIREBASE_REF_ADMIN);

        var generatedHash = db.push().set({
            description: message.description,
            content: message.content,
            date: firebase.database.ServerValue.TIMESTAMP,
            user: hash
        });
    };

    /**
     * Validate the login form before do the firebase connection
     * @returns {_L1.validateForm.stat|Boolean}
     */
    this.validateForm = function () {
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
        }

        return stat;
    };
};