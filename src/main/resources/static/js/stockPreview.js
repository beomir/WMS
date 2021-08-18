function rowClicked(value) {
    location.href = value;
}

hideText = $('#hideText').text()
showText = $('#showText').text()
$('#showText').remove()
$('#hideText').remove()

$("[name='hdRow']").click(function(){
    $('#headerHdDetails').hide();

    if($('#hdDetailsTables').attr("name") == 'off'){
        $('#hdDetailsTables').show(500);
        $('#hdDetailsTables').attr("name","on")
        $("[name='hdRow']").attr("title",hideText);
    }
    else{
        $('#hdDetailsTables').hide(500);
        $('#hdDetailsTables').attr("name","off")
        $("[name='hdRow']").attr("title",showText);
    }

})


const toggleModal = () => {
    document.querySelector('.modal')
        .classList.toggle('modal--hidden');
    document.querySelector('.overlay')
        .classList.toggle('overlay--hidden');
}


$( ".show-modal2" ).click(function(){
    toggleModal();
    $("[name='placeForLocationName']").text($('#locationName').text());
    $("[name='placeForLocationName']").val($('#locationName').text());
})

document.querySelector('.overlay')
    .addEventListener('click', toggleModal);


$( ".modal__close-bar span" ).click(function() {
    toggleModal()
})

function iconFlashing(){
    console.log("iconFlashing works")
    let iconFlashingInterval = setInterval(function(){ $("#informationIcon").toggleClass('makeItBigger--active') }, 1000);

    $( "#informationIcon" ).mouseenter(function() {
        clearInterval(iconFlashingInterval);
        $("#informationIcon").removeClass('makeItBigger--active')
        console.log("done")
    })
}

$("[name='articleNumber']").click(function(){
    console.log($('#inHowManyLocations').attr('max'))
    if($('#inHowManyLocations').val() == '' || $('#inHowManyLocations').val() == null){
        alert("Please do not forget fulfill field about number of locations")
        returnToPreviousPage();
        return false;
    }
    else if($('#inHowManyLocations').val() > $('#inHowManyLocations').attr('max')){
        alert("Entered Value is bigger than allowed value. Please contact with administrator if you want increase this value")
        returnToPreviousPage();
        return false;
    }
    else{
        $('<input>').attr({
            type: 'hidden',
            name: 'articleNumberInLocations',
            value: $(this).text()
        }).appendTo('form');
        $('#searchNearbyLocationsForArticle').submit();
    }
});


function returnToPreviousPage() {
    window.history.forward(-1)
}

console.log("valueToPreview: " + $("[name='valueToPreview']").val())