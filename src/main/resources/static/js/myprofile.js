var dLayout = null; 
var dLayoutShow = null;
var text = "";

$(function() {
	$("#modDescription").on("click", editMode);
	dLayout = $("#descriptionLayout");
	dLayoutShow = dLayout.contents();
	var text = $("#description").text();
})

var updateDescription = function() {
	text = $("#description-text-area").text();
	$.ajax({
		type: 'POST',
		url: 'updateDescription',
		data: {
			description: text	
		},
		success: showMode,
	});
}

var editMode = function() {
	$("#modDescription").hide();
	dLayout.empty();
	dLayout.append("<textarea id='description-text-area' class='form-control'>" + text + "</textarea>");
	dLayout.append("<button type='button' id='confirmMod' class='btn btn-outline-primary btn-sm'>Conferma Modifica</button>");
	$("#confirmMod").css("margin-top", "10px");
	$("#confirmMod").on("click", updateDescription);
}

var showMode = function() {
	$("#modDescription").show();
	dLayout.empty();
	dLayout.append(dLayoutShow);
}