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

function initializeFireBaseMessaging() {
	messaging.requestPermission().then(function() {
		return messaging.getToken();
	}).then(function(token) {
		
		$.ajax({
			type: "POST",
			url: "/set-client",
			data: {
				token: token,
			},
			success: function(data) {
				if(data == "error") 
					errorMessage();
				else if(data == "already present") {
					Swal.fire({
						icon: "error",
						title: "Notifiche già attive",
						text: "Hai già attivato le notifiche da questo dispositivo."
					})
				}
			},
			error: errorMessage
		});
		
		$("#notification-bell").removeClass("far");
		$("#notification-bell").addClass("fas");
	});
}

function suppressFireBaseMessaging() {
	messaging.requestPermission().then(function() {
		return messaging.getToken();
	}).then(function(token) {
		
		$.ajax({
			type: "POST",
			url: "/unset-client",
			data: {
				token: token
			},
			success: function(data) {
				if(data == "error")
					errorMessage();
				else {
					$("#notification-bell").removeClass("fas");
					$("#notification-bell").addClass("far");
				}
			},
			error: errorMessage
		});
	});
}

function checkFireBaseMessaging() {
	messaging.requestPermission().then(function() {
		return messaging.getToken();
	}).then(function(token) {
		
		$.ajax({
			type: "POST",
			url: "/check-client",
			data: {
				token: token
			},
			success: function(data) {
				if(data == "already present") {
					$("#notification-bell").addClass("fas fa-bell");
				}
				else if(data == "not present") {
					$("#notification-bell").addClass("far fa-bell");
				}
				else
					errorMessage();
			},
			error: errorMessage
		})
	});
}

messaging.onMessage(function(payload) {
	const notificationOption = {
		body: payload.notification.body,
		icon: payload.notification.icon
	}
	
	if(Notification.permission == "granted") {
		var notification = new Notification(payload.notification.title, notificationOption);
		notification.onclick = function(ev) {
			ev.preventDefault();
			notification.close();
		}		
	}
})

messaging.onTokenRefresh(function() {
	messaging.getToken().then(function(newToken) {
		
		$.ajax({
			type: "POST",
			url: "/set-new-client",
			data: {
				token: newToken,
			}
		});
		
	});
})