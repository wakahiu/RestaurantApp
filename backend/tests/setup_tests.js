// Connect to the database.
function connectDB(callback) {
    mongoClient.connect(dbConfig.testDBURL, function(err, db) {
        assert.equal(null, err);
        reader_test_db = db;
        console.log("Connected correctly to server");
        callback(0);
    });
}

// Drop the user collection. This ensures that our database is in a known
// starting state.
function dropUserCollection(callback) {
    console.log("dropUserCollection");
    user = reader_test_db.collection('user');
    if (undefined != user) {
        user.drop(function(err, reply) {
            console.log('user collection dropped');
            callback(0);
        });
    } else {
        callback(0);
    }
}

// Drop the Restaurant collection. This ensures that our database is in a known
// starting state.
function dropRestaurantCollection(callback) {
    console.log("dropRestaurantCollection");
    user_feed_entry = reader_test_db.collection('user_feed_entry');
    if (undefined != user_feed_entry) {
        user_feed_entry.drop(function(err, reply) {
            console.log('user_feed_entry collection dropped');
            callback(0);
        });
    } else {
        callback(0);
    }
}

// Connect to Stormpath and delete all the users in our test application.

function getApplication(callback) {
        console.log("getApplication");
        client.getApplications({
            name: SP_APP_NAME
        }, function(err, applications) {
            console.log(applications);
            if (err) {
                log("Error in getApplications");
                throw err;
            }
            app = applications.items[0];
            callback(0);
        });
    },
    function deleteTestAccounts(callback) {
        app.getAccounts({
            email: TU_EMAIL_REGEX
        }, function(err, accounts) {
            if (err) throw err;
            accounts.items.forEach(function deleteAccount(account) {
                account.delete(function deleteError(err) {
                    if (err) throw err;
                });
            });
            callback(0);
        });
    }