$('#password1, #password2').on('keyup', function () {
    if($('#password1').val() =='')
    {
        $('#passSNotEmpty').removeClass("d-none");
        $('#passdif').addClass("d-none");
        $('#itsOK').addClass("d-none");
    }
    else if ($('#password1').val() != $('#password2').val()) {
        $('#passSNotEmpty').addClass("d-none");
        $('#passdif').removeClass("d-none");
        $('#itsOK').addClass("d-none");
    }
    else {
        $('#passSNotEmpty').addClass("d-none");
        $('#passdif').addClass("d-none");
        $('#itsOK').removeClass("d-none");
    }
});
function checkPasswordAndName() {

    if($('#password1').val() != $('#password2').val() )
    {
        alert($('#passdif').text())
        returnToPreviousPage();
        return false;
    }
    else if($('#password1').val() =='' || $('#password2').val() ==''){
        alert($('#passSNotEmpty').text())
        returnToPreviousPage();
        return false;
    }
}
function returnToPreviousPage() {
    window.history.forward(-1)
}