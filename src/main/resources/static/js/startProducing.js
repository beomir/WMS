$( ".finishProduct" ).click(function(ev) {
    if($("#" + ev.target.textContent + "finishProduct").attr('name') == 'close'){
        $("." + ev.target.textContent + 'intermediate').show(500)
        $("#" + ev.target.textContent + 'finishProduct').attr('name', 'open');
    }
    else{
        $("." + ev.target.textContent + 'intermediate').hide(500)
        $("#" + ev.target.textContent + 'finishProduct').attr('name', 'close');
    }
    console.log(ev.target.textContent + ': ' +  $("#" + ev.target.textContent + "finishProduct").attr('name'))

});

$('.finishProduct').mouseover(function(e) {
    let target = $(e.target)
    let elId = target.attr('id');
    $("#" + elId).css("background-color", "brown")
});

$('.finishProduct').mouseout(function(e) {
    let target = $(e.target)
    let elId = target.attr('id');
    $("#" + elId).css("background-color", "green")
});
