var auth = new function () {
    this.unauth = function () {
        localStorage['user'] = null;
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
                        $.each(snapshot.val(), function (index, values) {
                            if (values.password == $('#password').val()) {
                                auth.storeUser(index, values);
                                stat = true;
                                window.location = 'index.html';
                            }
                        });
                        if (!stat) {
                            toastr.error('Usuário e/ou senha incorretos');
                        }
                    });
        }
    };

    this.storeUser = function (hash, fields) {
        localStorage['user']['hash'] = hash;
        localStorage['user']['name'] = fields.name;
        localStorage['user']['email'] = fields.email;
        localStorage['user']['picture'] = fields.picture;
    };


    this.validateForm = function () {
        var stat = true;
        var errorMsg = [];

        if ($('#username').val() == null || $('#username').val().trim().length < 3) {
            stat = false;
            errorMsg.push('Username must have more than 2 characters');
        }
        if ($('#password').val() == null || $('#password').val().trim().length < 3) {
            stat = false;
            errorMsg.push('Password must have more than 2 characters!');
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