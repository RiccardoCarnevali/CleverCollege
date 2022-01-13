var video = null;
var canvas = null;
var processFrameTask = null;

$(function() {
	loadUserMedia(false);
	video = document.querySelector('.video-capture');
	canvas = document.querySelector('canvas');
	
	//carica i luoghi
	loadedLocations = [];
	
	like = '';
	loadLocations(false);

	$("#locationSearchBar").on("input", function() {
		like = $(this).val();
		loadLocations(false);
	});
});

function loadLocations(showMore) {
	if (showMore)
		offset = loadedLocations.length;
	else
		offset = 0;

	$.ajax({
		type: 'post',
		url: '/getLocations',
		data: {
			like: like,
			offset: offset
		},
		success: function(data) {
			$('#showMoreButton').remove();
			if (areEquals(data.slice(0, 15), loadedLocations) && loadedLocations.length != 0) {
				if (data.length == 16) {
					$("#locationList").append('<button class="btn btn-outline-primary"'
						+ ' id="showMoreButton">Mostra altri</button>');
					$("#showMoreButton").off().on("click", function() {
						loadLocations(true);
					});
				}
				return;
			}

			if (!showMore) {
				loadedLocations = [];
				$("#locations").empty();
			}

			loadedLocations = loadedLocations.concat(data.slice(0, 15));
			if (loadedLocations.length <= 15)
				index = 0;

			$('#locationList').css('overflow-y', 'scroll');
			if (loadedLocations.length <= 3) {
				$('#locationList').css('overflow-y', 'hidden');
			}

			var locationList = $("#locations");
			for (; index < loadedLocations.length; index++) {
				if (loadedLocations[index])
					locationList.append('<li class="list-group-item location">' +
						'<div class="location-name">' + loadedLocations[index].name +
						'<span onclick="checkInto('+loadedLocations[index].id+')" class="clickable fas fa-door-open"'+
						'id="location'+ index +'"></span></div>');
			}
			if (data.length == 16) {
				$("#locationList").append('<button class="btn btn-outline-primary" id="showMoreButton">Mostra altri</button>');
				$("#showMoreButton").off().on("click", function() {
					loadLocations(true);
				});
			}
			
		},
		error: errorMessage
	});
}

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
			console.log(result);
			if(result != 'found') {
				console.log('no qr code found');
			} else {
				location.reload();
				loadUserMedia(false);
			}
		}
	});
}

function areEquals(location1, location2) {
	if (location1.length != location2.length)
		return false;

	for (let i = 0; i < location1.length; i++) {
		if (location1[i].id != location2[i].id)
			return false;
	}

	return true;
}

function checkInto(locationId) {
	$.ajax({
		type:'post',
		url:'check-in-by-id',
		data:{
			locationId: locationId
		},
		success: function() {
			location.reload();
			loadUserMedia(false);
		}
	});
}

