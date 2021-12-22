const functions = require("firebase-functions");
const admin = require("firebase-admin");
const express = require('express');
const bodyParser = require('body-parser');
const generateUUID = require('./util');

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



// Intialize Express server

const app = express();
const main = express();

main.use('/api/v1', app);
main.use(bodyParser.json());

//initialize the database and the collection 
const db = admin.firestore();
const userDetailsCollection = 'deviceTracker';

exports.webApi = functions.https.onRequest(main);

app.post('/generate-deviceId', async (req, res) => {

    try {

        const deviceUUID = generateUUID.generateUUID();

        const userUUID = req.body['userUUID'];
        const fcmToken = req.body['fcmToken'];

        
        const newDoc = await db.collection(userDetailsCollection).doc(deviceUUID).set({
            userUUID,
            fcmToken
        });

        deviceDetails = {
            deviceUUID
        };

        res.status(201).send(deviceDetails);
    } catch (error) {
        res.status(400).send(error)
    }

})