$(function() {
	$(".header").load("/header");
	$(".footer").load("/footer");
	
	window.onscroll = function() {

		if (document.documentElement.scrollTop > 80 || document.body.scrollTop > 80) {
			document.getElementsByClassName("navbar")[0].style.padding = "8px 10px";
			document.getElementById("logo").style.height = "55px";
		} else {
			document.getElementsByClassName("navbar")[0].style.padding = "20px 10px";
			document.getElementById("logo").style.height = "78px";
		}
	};
})