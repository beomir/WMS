const toggleModal = () => {
    document.querySelector('.modal')
        .classList.toggle('modal--hidden');
    document.querySelector('.overlay')
        .classList.toggle('overlay--hidden');
}
let appendMessage;
let clickedArticleStatus;

let textActive = $("#textModalMessageActivate").text()
let textDeactivate = $("#textModalMessageDeactivate").text()

$( ".show-modal" ).click(function(){
    clickedArticleStatus = $(this).attr("lang");
    console.log($(this).attr("lang"))
    console.log(clickedArticleStatus)
    if(clickedArticleStatus == "active"){
        $("#id").val($(this).attr('name'))
        $("#status").val($(this).attr('lang'))
        appendMessage = ': ' + $(this).attr('alt') + '?'
        $("#textModalMessage").text(textDeactivate + ' ' + appendMessage);
        toggleModal();
    }
    else{
        $("#id").val($(this).attr('name'))
        $("#status").val($(this).attr('lang'))
        appendMessage = ': ' + $(this).attr('alt') + '?'
        $("#textModalMessage").text(textActive + ' ' + appendMessage);
        toggleModal();
    }

})

document.querySelector('.overlay')
    .addEventListener('click', toggleModal);


$( ".modal__close-bar span" ).click(function() {
    toggleModal()
})


document.querySelector('.overlay')
    .addEventListener('click', toggleModal);
