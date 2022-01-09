/**
 * 
 */

function printQRCode() {
	var win = window.open('');
	win.document.write($('#qrCodeContainer').html());
	win.focus();
}