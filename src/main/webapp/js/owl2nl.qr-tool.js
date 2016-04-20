$(function() {
	$(".star_rating").each(function() {
		$(this).rating({
			showClear : false,
			showCaption : false
		}).on('rating.change', function(event, value, caption, target) {
			$(this).attr("value",value);
		}).on('rating.reset', function(event) {
			$(this).attr("value",0);
		});
	});
	$(".submit-button").button()
//  	$("#selectexperiment").button()
});

function checkSubmit() {
	var isChecked = $("input.star_rating[value!='0']").length == $("input.star_rating").length;
	return isChecked;
}
