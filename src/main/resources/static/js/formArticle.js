$('#width,#height,#depth').on('keyup', function (){
    let volume = document.getElementById('volume');
    let width = document.getElementById('width').value;
    let height = document.getElementById('height').value;
    let depth = document.getElementById('depth').value;

    if(width != "" && width != null && height != null && height != "" && depth != null && depth != ""){
        volume.innerHTML = "Article volume: " + (width * height * depth).toString() + " cm3";
        $('#volume').css('color', 'green');
        $('#volume').css('background-color', 'black');
        $('#volume').css('border', '2px solid');
        $('#volume').css('border-radius', '5px');
    }
    else{
        $('#volume').css('color', 'transparent');
        $('#volume').css('background-color', 'transparent');
    }

})

function checkValidation(){
    let width = document.getElementById('width').value;
    let height = document.getElementById('height').value;
    let depth = document.getElementById('depth').value;
    let weight = document.getElementById('weight').value;

    if(width == null || width == "" || width == 0.0 || width == 0){
        alert("Width must be bigger than 0")
        returnToPreviousPage();
        return false;
    }
    else if(height == null || height == "" || height == 0.0 || height == 0){
        alert("Height must be bigger than 0")
        returnToPreviousPage();
        return false;
    }
    else if(depth == null || depth == "" || depth == 0.0 || depth == 0){
        alert("Depth must be bigger than 0")
        returnToPreviousPage();
        return false;
    }
    else if(weight == null || weight == "" || weight == 0.0 || weight == 0){
        alert("Weight must be bigger than 0")
        returnToPreviousPage();
        return false;
    }
}

function returnToPreviousPage() {
    window.history.forward(-1)
}

const hide = document.getElementById("productionArticleDetails")
const productionArticle = document.getElementById("productionArticle")
let productionArticleConnection = document.getElementById("productionArticleConnection")
let production = document.getElementById("production")
let select = document.getElementById('selectId')
const articleNumber = document.getElementById("articleNumber")

const specialStorageZoneDetails = document.getElementById("specialStorageZoneDetails")
const specialStorageZone = document.getElementById("specialStorageZone");

hide.classList.add("d-none")
specialStorageZoneDetails.classList.add("d-none")

specialStorageZone.addEventListener("click",function(){
    if(!this.checked){
        $("#specialStorageZoneDetails").hide(400);
    }
    else{
        $("#specialStorageZoneDetails").show(400);
    }
    }
)

productionArticle.addEventListener("click", function (e) {
    if (!this.checked) {
        $("#hide").hide(400);
        // $("#productionArticleConnection").removeAttr('required').val("")
        // $("#quantityForFinishedProduct").removeAttr('required').val("")
        production.value = "false"
        productionArticleConnectionFunction();
    } else {
        $("#hide").show(400);
        // $("#productionArticleConnection").prop('required',true);
        // $("#quantityForFinishedProduct").prop('required',true);
        productionArticleConnectionFunction()
        production.value = "true"
    }
})
$('#productionArticle').click(function() {
    if ($(this).is(':checked')) {
        $("#productionArticleDetails").show(400);
        $('html, body').animate({
            scrollTop: $("#productionArticleDetails").offset().top
        }, 2000);
    } else {
        $("#productionArticleDetails").hide(400);
        console.log("Checkbox is unchecked.")
        $('html, body').animate({
            scrollTop: $("#top").offset().top
        }, 1000);
    }
});

select.addEventListener("change", function () {
    productionArticleConnectionFunction();

})

articleNumber.addEventListener("keyup", function () {
    productionArticleConnectionFunction();
});

function productionArticleConnectionFunction(){
    if(document.getElementById('selectId').value == "finish product"){
        $("#productionArticleConnection").prop('readonly',true);
        document.getElementById("productionArticleConnection").value = document.getElementById('articleNumber').value
        productionArticleConnection.innerHTML = document.getElementById('articleNumber').value
    }
    else if(!window.location.href.includes("formEditArticle")){
        productionArticleConnection.value = "";
        $("#productionArticleConnection").removeAttr('readonly').val("")

    }
}

if(window.location.href.indexOf("formEditArticle") > -1){
    if(document.getElementById('productionArticleStatus').innerHTML == "true"){
        $('#productionArticle').prop('checked', true);
        $("#productionArticleDetails").show(400);
    }
}
