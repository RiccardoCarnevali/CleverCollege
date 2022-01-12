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
		console.log("Notification permission")
		return messaging.getToken();
	}).then(function(token) {
		
		$.ajax({
			type: "POST",
			url: "/set-new-client",
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
		
		console.log("Token: " + token)
		$("#token").append(token);
	}).catch(function(reason) {
		console.log(reason);
		$("#token").append(reason);
	})
}

messaging.onMessage(function(payload) {
	console.log(payload);
	const notificationOption = {
		body: payload.notification.body,
		icon: payload.notification.icon
	}
	
	if(Notification.permission == "granted") {
		var notification = new Notification(payload.notification.title, notificationOption);
		
		notification.onclick = function(ev) {
			ev.preventDefault();
			window.open(payload.notification.click_action, "_blank");
			notification.close();
		}		
	}
})

messaging.onTokenRefresh(function() {
	messaging.getToken().then(function(newToken) {
		console.log("New token: " + newToken);
		
		$.ajax({
			type: "POST",
			url: "/set-new-client",
			data: {
				token: newToken,
			}
		});
		
	}).catch(function(reason) {
		console.log(reason);
	})
})

$(function() {
	$("#subscribe").on("click", initializeFireBaseMessaging);
	
	$("#sendMessage").on("click", function() {
		$.ajax({
			type: "POST",
			url: "/send-notification-to-all"
		})
	})
})