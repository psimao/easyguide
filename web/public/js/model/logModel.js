this.log = new function () {

    /**
     * Set the log in 'admin' child.
     * @param {type} hash user
     * @param {type} message object
     * @returns {undefined} void
     */
    this.setLog = function (hash, ref, message) {
        var db = firebase.database().ref(ref);

        db.push().set({
            description: message.description,
            content: message.content,
            date: firebase.database.ServerValue.TIMESTAMP,
            user: hash
        });
    };
};