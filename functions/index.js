const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendGiveDataAddedTrigger = functions.database.ref('/giver_data/{pushId}').onWrite((change, context) =>
	{
		const give = change.after.val();
		const payload = {
			notification : {
				title : 'New Food Added',
				body : `${give.titleData}`
			}
		};

	return admin.messaging().sendToTopic("giver_data",payload)
		.then(response =>
		{
			return console.log('Notification sent successfully', response);
		})
		.catch(error =>
		{
			return console.log('Notification sent unsucessful', error);
		});

	});

