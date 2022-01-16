$(function() {
	$(".header").load("/header", function() {
		window.onscroll = function() {
			if (document.documentElement.scrollTop > 80 || document.body.scrollTop > 80) {
				$('.navbar').css('padding', '8px 10px');
				$('#logo').css('height', '52px');
			} else {
				$('.navbar').css('padding', '20px 10px');
				$('#logo').css('height', '76px');
			}
		};
		
		var notificationBell = $("#notification-bell");
		
		if(Notification.permission == "granted") {
			checkFireBaseMessaging();
		}
		else
			notificationBell.addClass("far fa-bell")
			
		notificationBell.on("click", function() {
			if($(this).hasClass("fas")) {
				suppressFireBaseMessaging();
			}
			else {
				if(Notification.permission == "denied") {
					Swal.fire({
						icon: "error",
						title: "Impossibile attivare le notifiche",
						text: "Il permesso di inviare notifiche è stato negato"
					});
				}
				else
					initializeFireBaseMessaging();
			}
		})
	});
	$(".footer").load("/footer");
})