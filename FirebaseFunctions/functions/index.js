const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

var db = admin.firestore();

//const firestore = admin.firestore;
//const settings = {/ your settings... / timestampsInSnapshots: true};
//firestore.settings(settings);

exports.sendPush2 = functions.firestore.document('Events/{eventId}').collection('chatRoom')
    .onUpdate((change, context) => {

        const newValue = change.after.data();
        const previousValue = change.before.data();

        const usersAfterChange = newValue.usersEntered;
        const usersBeforeChange = previousValue.usersEntered;

        const eventCreatorUserId = newValue.idOfTheUserWhoCreatedIt
        const eventDescription = previousValue.eventDescription;
        const eventId = newValue.eventId;

        const listOfUsers = previousValue.listOfUsersParticipatingInEvent;

        if (usersAfterChange == usersBeforeChange) return null;

        let messageText = "Novi korisnik se pridružio "
        if (usersAfterChange < usersBeforeChange) {
            messageText = "Jedan korisnik je izašao iz "
            listOfUsers = newValue.listOfUsersParticipatingInEvent;
        }
        let users = [];
        var docRef = db.collection('Users');
        docRef.get()
            .then(snapshot => {
                snapshot.forEach(doc => {
                    console.log(doc.id, '=>', doc.data());
                    users.push(doc.data());
                });

                let tokens = [];
                for (let user of users) {
                    if (listOfUsers.indexOf(user.userId) > -1){
                      tokens.push(user.userToken)
                    }
                }
                let payload = {
                    data: {
                        data_type : "direct_message",
                        title: messageText + eventDescription,
                        message: messageText + eventDescription,
                        messageId: eventId,
                        eventId: eventId,
                    }
                };

				        let log = admin.messaging().sendToDevice(tokens, payload);
				        console.log("Result: ", log);
            })

        return 0;
    });
