$(document).ready(function () {
    comom.fillHeader();
});

this.comom = new function () {

    /*
     * The timeout is equivalent to 1 day;
     */
    this.timeout = 86400000;

    /**
     * Fill the header with the current user information.
     * @returns {undefined}
     */
    this.fillHeader = function () {
        firebase
                .database()
                .ref('/admin/' + localStorage['admin'])
                .on('value', function (snapshot) {
                    var stat = false;
                    if (snapshot.val()) {
                        document.getElementById('image_profile').src = snapshot.val().image_profile;
                        document.getElementById('username').innerHTML = snapshot.val().name;
                        this.checkUserLogged(snapshot.val());
                    } else {
                        window.location = 'login.html';
                    }
                    return;
                });
    };

    this.checkUserLogged = function (admin) {
        if (admin.dt_last_login) {
            if ((firebase.database.ServerValue.TIMESTAMP - admin.dt_last_login) >= this.timeout) {
                window.location = 'login.html';
            }
        } else {
            window.location = 'login.html';
        }
    };
};