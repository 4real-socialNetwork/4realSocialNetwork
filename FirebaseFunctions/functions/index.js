const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();

var db = admin.firestore();


exports.eventJoining = functions.firestore.document('Events/{eventId}')
    .onWrite((change, context) => {

        const newValue = change.after.data();
        const previousValue = change.before.data();

        const usersAfterChange = newValue.usersEntered;
        const usersBeforeChange = previousValue.usersEntered;

        const eventCreatorUserId = newValue.idOfTheUserWhoCreatedIt
        const eventDescription = previousValue.eventDescription;
        const eventId = newValue.eventId;

        const listOfUsers = previousValue.listOfUsersParticipatingInEvent;

        if (usersAfterChange == usersBeforeChange) return null;

        let messageText = "Novi korisnik se pridružio"
        if (usersAfterChange < usersBeforeChange) {
            messageText = "Jedan korisnik je izašao"
            listOfUsers = newValue.listOfUsersParticipatingInEvent;
        }
        let users = [];
        var docRef = db.collection('Users');
        docRef.get()
            .then(snapshot => {
                snapshot.forEach(doc => {
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
                        data_type : "event_joining",
                        title: messageText,
                        message: eventDescription,
                        messageId: eventId,
                        extraId: eventId,
                    }
                };

				        let log = admin.messaging().sendToDevice(tokens, payload);

            })

        return 0;
    });

exports.chatRoomEvents = functions.firestore.document('Events/{eventId}/chatRoom/{chatId}')
    .onCreate((snap, context) => {

		const wholeDocument = snap.data();
		const usersInChat = wholeDocument.usersInChat;
		const senderId = wholeDocument.uid;
		const dataMessage = wholeDocument.message
		const senderName = wholeDocument.name;
		const eventId = wholeDocument.idOfEventOfMessage;

        let users = [];

        var docRef = db.collection('Users');
        docRef.get()
            .then(snapshot => {
                snapshot.forEach(doc => {
                    users.push(doc.data());
                });

                let tokens = [];
                for (let user of users) {
                    if (usersInChat.indexOf(user.userId) > -1 && user.userId != senderId){
                      tokens.push(user.userToken)
                    }
                }
                let payload = {
                    data: {
                        data_type : "event_message",
						            title:senderName,
                        message: dataMessage,
						            messageId: eventId,
                        extraId: eventId,
                    }
                };

				        let log = admin.messaging().sendToDevice(tokens, payload);
				        console.log("Result: ", log);
            });

        return 0;
    });

exports.chatRoomGroups = functions.firestore.document('Groups/{groupId}/chatRoom/{chatId}')
    .onCreate((snap, context) => {

		const wholeDocument = snap.data();
		const usersInChat = wholeDocument.usersInChat;
		const senderId = wholeDocument.uid;
		const dataMessage = wholeDocument.message
		const senderName = wholeDocument.name;
		const eventId = wholeDocument.idOfGroupOfMessage;

        let users = [];

        var docRef = db.collection('Users');
        docRef.get()
            .then(snapshot => {
                snapshot.forEach(doc => {
                    users.push(doc.data());
                });

                let tokens = [];
                for (let user of users) {
                    if (usersInChat.indexOf(user.userId) > -1 && user.userId != senderId){
                      tokens.push(user.userToken)
                    }
                }
                let payload = {
                    data: {
                        data_type : "group_message",
						            title:senderName,
                        message: dataMessage,
						            messageId: eventId,
                        extraId: eventId,
                    }
                };

				        let log = admin.messaging().sendToDevice(tokens, payload);
				        console.log("Result: ", log);
            });

        return 0;
    });

exports.eventRequest = functions.firestore.document('Users/{userId}/EventRequests/{pushId}')
        .onCreate((snap, context) => {

            const wholeDocument = snap.data();
            const recieverId = wholeDocument.reciverDocRef;
            const senderName = wholeDocument.senderName;
            const eventId = wholeDocument.eventId;
            const eventActivity = wholeDocument.eventActivity;
            const dataMessage = "Pridruži se mom eventu: ";

            let users = [];

            var docRef = db.collection('Users');
            docRef.get()
                .then(snapshot => {
                    snapshot.forEach(doc => {
                        users.push(doc.data());
                    });

                    let tokens = [];
                    for (let user of users) {
                        if (user.userId == recieverId){
                          tokens.push(user.userToken)
                        }
                    }
                    let payload = {
                        data: {
                            data_type : "event_request",
                            title: senderName,
                            message: dataMessage + eventActivity,
                            messageId: eventId,
                            extraId: eventId,

                        }
                    };

    				        let log = admin.messaging().sendToDevice(tokens, payload);
    				        console.log("Result: ", log);
                });

            return 0;
        });

exports.friendRequest = functions.firestore.document('Users/{userId}/FriendRequest/{pushId}')
                .onCreate((snap, context) => {

                    const wholeDocument = snap.data();
                    const recieverId = wholeDocument.reciverId;
                    const senderDocRef = wholeDocument.senderDocRef;
                    const senderName = wholeDocument.senderName;
                    const dataMessage = "Zahjev za prijateljstvo";

                    let users = [];

                    var docRef = db.collection('Users');
                    docRef.get()
                        .then(snapshot => {
                            snapshot.forEach(doc => {
                                users.push(doc.data());
                            });

                            let tokens = [];
                            for (let user of users) {
                                if (user.userId == recieverId){
                                  tokens.push(user.userToken)
                                }
                            }
                            let payload = {
                                data: {
                                    data_type : "friend_request",
                                    title: senderName,
                                    message: dataMessage,
                                    messageId: senderDocRef,
                                    extraId: senderDocRef,
                                }
                            };
            				        let log = admin.messaging().sendToDevice(tokens, payload);
            				        console.log("Result: ", log);
                        });

                    return 0;
                });
