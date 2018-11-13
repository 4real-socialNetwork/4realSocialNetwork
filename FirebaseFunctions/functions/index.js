const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

var db = admin.firestore();

//const firestore = admin.firestore;
//const settings = {/* your settings... */ timestampsInSnapshots: true};
//firestore.settings(settings);

exports.sendPush2 = functions.firestore.document('Events/{eventId}')
    .onUpdate((change, context) => {


        const newValue = change.after.data();
        const previousValue = change.before.data();

        const usersAfterChange = newValue.usersEntered;
        console.log("usersAfterChange: ", usersAfterChange);

        const usersBeforeChange = previousValue.usersEntered;
        console.log("usersBeforeChange ", usersBeforeChange);

        const userId = newValue.idOfTheUserWhoCreatedIt;
        console.log("userId: ", userId);

        const listOfUsers = newValue.listOfUsersParticipatingInEvent;
        console.log("listOfUsers ", listOfUsers);

        if (usersAfterChange == usersBeforeChange) return null;

        let message1 = "Jedan korisnik je izašao iz vašeg eventa"
        if (usersAfterChange > usersBeforeChange) {
            message1 = "Novi korisnik se pridružio Vašem eventu"
        }
        let users = [];
        var docRef = db.collection('Users');
        docRef.get()
            .then(snapshot => {
                snapshot.forEach(doc => {
                    console.log(doc.id, '=>', doc.data());
                    users.push(doc.data());
                    console.log("users inside", users);
                });

                // ovdje imaš usere pa im radiš što oćeš
                let tokens = [];
                for (let user of users) {
                    //if (listOfUsers.contains(user.userId){
                    tokens.push(user.userToken)
                    //}
                }

                let payload = {
                    notification: {
                        title: message1,
                        body: `Body`,
                    }
                };

                console.log("tokens su: ", tokens);
				
				let log = admin.messaging().sendToDevice(tokens, payload);
				console.log("Result: ", log);
            })
        return 0;
    });