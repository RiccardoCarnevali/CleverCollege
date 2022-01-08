var video = null;
var canvas = null;
var processFrameTask = null;

$(function() {
	loadUserMedia(false);
	video = document.querySelector('.video-capture');
	canvas = document.querySelector('canvas');
});

function loadUserMedia(show) {
	if (show) {
		navigator.mediaDevices.getUserMedia({ video: { facingMode: 'environment' } })
			.then(function(stream) {
				video.srcObject = stream;
				video.onloadedmetadata = function(e) {
					video.play();
					processFrameTask = setInterval(processFrame, 4000);
				}
				$('#showStreamBttn').off('click').on('click', function() {
					$('#showStreamBttn').html('Apri Fotocamera');
					loadUserMedia(false);
				});
			})
			.catch({

			});
	} else {
		if (video != null) {
			video.srcObject.getTracks()[0].stop();
			clearInterval(processFrameTask);
		}
		$('#showStreamBttn').off('click').on('click', function() {
			$('#showStreamBttn').html('Chiudi Fotocamera');
			loadUserMedia(true);
		});
	}

}

//take a screen of the video
async function processFrame() {
	if (video == null || video.srcObject == null)
		return;
	canvas.width = video.videoWidth;
	canvas.height = video.videoHeight;
	canvas.getContext("2d").drawImage(video, 0, 0);
	
	$.ajax({
		type: 'post',
		url: 'process-qrcode',
		data: {
			imageDataURL:canvas.toDataURL('image/jpeg')
		},
		success: function(result) {
			if(result.data != null) {
				console.log('no qr code found');
			} else {
				loadUserMedia(false);
			}
		}
	})
}


