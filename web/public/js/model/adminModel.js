/* global auth, firebase */

var admin = new function () {
    /**
     * Store the admin on localStorage to keep logged.
     * @param {type} hash user
     * @returns {undefined} void
     */
    this.storeAdmin = function (hash, adminObject) {
        localStorage.setItem(auth.LOCAL_STORAGE_ADMIN, hash);
        adminObject.dt_last_login = firebase.database.ServerValue.TIMESTAMP;
        admin.updateAdmin(hash, adminObject);
    };

    /**
     * Create log for access and update the timeout
     * @param {type} hash
     * @returns {undefined}
     */
    this.updateAdmin = function (hash, admin) {
        var db = firebase.database().ref(auth.FIREBASE_REF_ADMIN + "/" + hash);
        admin.dt_changed = firebase.database.ServerValue.TIMESTAMP;
        return db.update(admin);
    };

    /**
     * Add the admin in firebase
     * @param {type} admin
     * @returns hash generated
     */
    this.addAdmin = function (admin) {
        var db = firebase.database().ref(auth.FIREBASE_REF_ADMIN);
        admin.dt_created = firebase.database.ServerValue.TIMESTAMP;
        return db.push(admin);
    };

    this.delAdmin = function (key) {
        var db = firebase.database().ref(auth.FIREBASE_REF_ADMIN);
        return db.child(key).remove();
    };
};