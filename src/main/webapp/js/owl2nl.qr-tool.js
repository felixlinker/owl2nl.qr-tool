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
    if(changeExperiment) {
        return true;
    }
    // if this is a star rating page
    if($("input.star_rating").length) {
        return $("input.star_rating[value!='0']").length > 0;
    }
    // if this is a radio button page
    if($("input[name='chosenInstance']").length) {
        return $("input[name='chosenInstance']:checked").length > 0;
    }
    console.log('There is no validity check for this page!');
    return true;
}