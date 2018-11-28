const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

var db = admin.firestore();

//const firestore = admin.firestore;
//const settings = {/ your settings... / timestampsInSnapshots: true};
//firestore.settings(settings);


exports.sendPush2 = functions.firestore.document('Events/{eventId}/chatRoom')
    .onCreate((change, context) => {

        const newValue = change.after.data();
        const previousValue = change.before.data(); // treba prikazati zadnju izmjenu u notification poruci putem ovoga
	
		
		let currentEventUrl = change.doc.ref.path.split("/").slice(0, ) //path to eventa unutar kojeg je chatRoom 
		currentEventUrl = currentEventUrl.split("/").slice(0, currentEventUrl.split("/").length - 1).join("/")
		var docRef = db.collection(currentEventUrl);
        docRef.get()
            .then(snapshot => {
				const listOfUsers = snapshot.listOfUsersParticipatingInEvent; //dobijemo usere koji su u eventu
				
				//dalje nije dirao
				//sretno care
				
				var docRef = db.collection('Users');
				docRef.get()
					.then(snapshot => {
						snapshot.forEach(doc => {
							console.log(doc.id, '=>', doc.data());
							users.push(doc.data());
						});
			})
       
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
