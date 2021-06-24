$( ".finishProduct" ).click(function(ev) {
    if($("#" + ev.target.textContent + "finishProduct").attr('name') == 'close'){
        $("." + ev.target.textContent + 'intermediate').show(500)
        $("#" + ev.target.textContent + 'finishProduct').attr('name', 'open');
        $("#" + ev.target.textContent + 'finishProduct').children().last().attr("src","/images/arrowsup20x20.png")
    }
    else{
        $("." + ev.target.textContent + 'intermediate').hide(500)
        $("#" + ev.target.textContent + 'finishProduct').attr('name', 'close');
        $("#" + ev.target.textContent + 'finishProduct').children().last().attr("src","/images/arrowsdown20x20.png")
    }
    console.log(ev.target.textContent + ': ' +  $("#" + ev.target.textContent + "finishProduct").attr('name'))

});

$('.finishProduct').mouseover(function(e) {
    if ($(e.target).attr('name').includes('finishProduct') ) {
        let target = $(e.target)
        let elId = target.parent().attr('id');
        $("#" + elId).css("background-color", "brown")
    } else {

    let target = $(e.target)
    let elId = target.attr('id');
    $("#" + elId).css("background-color", "brown")
}
});

$('.finishProduct').mouseout(function(e) {
    if ($(e.target).attr('name').includes('finishProduct') ) {
        let target = $(e.target)
        let elId = target.parent().attr('id');
        $("#" + elId).css("background-color", "green")
    } else {

    let target = $(e.target)
    let elId = target.attr('id');
    $("#" + elId).css("background-color", "green")
    }
});
