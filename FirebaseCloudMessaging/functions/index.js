const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

exports.covidContactNotification = functions.firestore.document("CovidContact/{docId}").onCreate(
    // snapshot represents the document that was added to the collection
    (snapshot, context) => {
        var contacts = snapshot.data().contacts;
        var  uids = [];
        contacts.forEach(function (contact) {
            var uid = contact.uid;
            uids.push(uid);
        });

        admin.firestore().collection("Users").get().then(
            result => {
                var registrationTokens = [];
                result.docs.forEach(
                    tokenDocument => {
                        if (uids.includes(tokenDocument.id)) {
                            registrationTokens.push(tokenDocument.data().FCM_Token);
                        }
                    }
                );
                admin.messaging().sendMulticast(
                    {
                        tokens: registrationTokens,
                        notification: {
                            title: "COVID PATIENT CONTACTED",
                            body: snapshot.data().message
                        }
                    }
                );
            }
        )
    }
);
