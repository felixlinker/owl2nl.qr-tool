var changeExperiment = false;

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
    $("#submit").button()
    if ($("#selectexperiment").length) {
        $("#selectexperiment").button();
        $("#selectexperiment").click(function(event){
            changeExperiment = true;
        });
    }
});

function checkSubmit() {
    var isChecked = changeExperiment || ($("input.star_rating[value!='0']").length == $("input.star_rating").length);
    return isChecked;
}