const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.sendGiveDataAddedTrigger = functions.database.ref('/giver_data/{pushId}').onWrite(event =>
	{
		const give = event.data.val();
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

	