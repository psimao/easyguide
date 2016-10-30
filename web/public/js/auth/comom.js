$(document).ready(function () {
    comom.fillHeader();
});

this.comom = new function () {

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
                        document.getElementById('image_profile').src = snapshot.val().picture;
                        document.getElementById('username').innerHTML = snapshot.val().name + " " + snapshot.val().last_name;
                        //$('#username').val(snapshot.val().name + " " + snapshot.val().last_name);
                    }
                    return;
                });
    };
};