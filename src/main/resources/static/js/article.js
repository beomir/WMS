const toggleModal = () => {
    document.querySelector('.modal')
        .classList.toggle('modal--hidden');
    document.querySelector('.overlay')
        .classList.toggle('overlay--hidden');
}
let appendMessage;

$( ".show-modal" ).click(function(){
    $("#id").val($(this).attr('name'))
    appendMessage = ': ' + $(this).attr('alt') + '?'
    $("#textModalMessage").append(appendMessage)
    toggleModal();
})

document.querySelector('.overlay')
    .addEventListener('click', toggleModal);


$( ".modal__close-bar span" ).click(function() {
    $("#textModalMessage").text($("#textModalMessage").text().replace(appendMessage,""));
    toggleModal()
})


document.querySelector('.overlay')
    .addEventListener('click', toggleModal);
