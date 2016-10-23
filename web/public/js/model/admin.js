this.admin = new function () {
    /**
     * Store the admin on localStorage to keep logged.
     * @param {type} hash user
     * @returns {undefined} void
     */
    this.storeAdmin = function (hash) {
        localStorage.setItem(auth.LOCAL_STORAGE_ADMIN, hash);
        admin.updateAdmin(hash);
    };

    /**
     * Create log for access and update the timeout
     * @param {type} hash
     * @returns {undefined}
     */
    this.updateAdmin = function (hash) {
        var db = firebase.database().ref(auth.FIREBASE_REF_ADMIN + "/" + hash);

        // Update last access.
        db.update({
            last_access: firebase.database.ServerValue.TIMESTAMP
        });
    };
};

