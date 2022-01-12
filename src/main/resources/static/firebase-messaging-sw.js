importScripts("https://www.gstatic.com/firebasejs/8.2.4/firebase-app.js");
importScripts("https://www.gstatic.com/firebasejs/8.2.4/firebase-messaging.js");
  
  const firebaseConfig = {

    apiKey: "AIzaSyCXWS6iyJ04XdRVQ-u7fK_TFsZBg9Wf-4Y",

    authDomain: "clevercollege-cc9ef.firebaseapp.com",

    projectId: "clevercollege-cc9ef",

    storageBucket: "clevercollege-cc9ef.appspot.com",

    messagingSenderId: "1085236438917",

    appId: "1:1085236438917:web:e89ae23198ce7ee40a86ec"

  };

firebase.initializeApp(firebaseConfig);

const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload) {
	console.log(payload);
	const notification = JSON.parse(payload);
	const notificationOption = {
		body: notification.body,
		icon: notification.icon
	}
	
	return self.registration.showNotification(payload.notification.title, notificationOption);
})