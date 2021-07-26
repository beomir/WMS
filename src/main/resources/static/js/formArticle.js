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
    if(window.location.href.includes("formEditArticle") && finishProductStatus == false){
        alert("Finish product: " + productionArticleConnectionForEdit.val() + " not exists")
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
        $("#productionArticleConnection").removeAttr('required').val("")
        // $("#quantityForFinishedProduct").removeAttr('required').val("")
        productionArticleConnectionFunction();
    } else {
        $("#hide").show(400);
        // $("#productionArticleConnection").prop('required',true);
        // $("#quantityForFinishedProduct").prop('required',true);
        productionArticleConnectionFunction()
        $("#productionArticleConnection").prop('required',true)
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
    if(window.location.href.includes("formEditArticle")){
        changeOptionForIntermediate();
    }
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
    else{

        productionArticleConnection.value = "";
        $("#productionArticleConnection").removeAttr('readonly').val("")
    }
}

if(window.location.href.indexOf("formEditArticle") > -1){
    if(document.getElementById('productionArticleStatus').innerHTML == "true"){
        $('#productionArticle').prop('checked', true);
        $("#productionArticleDetails").show(400);
        production.value = "true"
    }
    /////////////// 04.06.2021
    const storageZoneValue = document.getElementById("storageZone").textContent;
    console.log(storageZoneValue);
    if(storageZoneValue != "NotAssigned"){
        $('#specialStorageZone').prop('checked', true);
        $("#specialStorageZoneDetails").show(400);
    }
    if(storageZoneValue=="NotAssigned"){
        $('#specialStorageZoneDetail option[value=""]').attr('selected','selected');
    }

}

/////////// 01.06.2021

let productionLocation = document.getElementsByName("productionLocation");
let productionWarehouse = document.getElementsByName("productionWarehouse");
let productionLocationId = document.getElementsByName("productionLocationId");

let chosenWarehouse = document.getElementById("chosenWarehouse");
let array = []
const productionLocationHeader = document.getElementById("productionLocationHeader");

chooseProductionLocation();

chosenWarehouse.addEventListener("change",function(){
    chooseProductionLocation();
})

function chooseProductionLocation(){
    const productionLocationDetail = document.getElementById("productionLocationDetail");
    let selectList = document.createElement("select");
    productionLocationDetail.remove();
    array = [];
    for(let i = 0; i < productionWarehouse.length;i++){
        // console.log("next warehouse: " + i + ", " +  productionWarehouse[i].textContent)
        // console.log("selected warehouse by option" + chosenWarehouse.options[chosenWarehouse.selectedIndex].text)
        if(productionWarehouse[i].textContent == chosenWarehouse.options[chosenWarehouse.selectedIndex].text){
            // console.log("found: " + productionLocation[i].textContent + ", " + productionWarehouse[i].textContent + ", " +  productionWarehouseId[i].textContent)
            array.push({val: productionLocationId[i].textContent, text: productionLocation[i].textContent})
        }
    }
    console.log(array)

    selectList.id = "productionLocationDetail";
    selectList.name = "location";
    selectList.classList.add("select-css");
    productionLocationHeader.appendChild(selectList);
    for (let i = 0; i < array.length; i++) {
        let option = document.createElement("option");
        option.value = array[i].val;
        option.text = array[i].text;
        selectList.appendChild(option);
    }
    document.getElementById("productionLocationHeader").style.display = "none";
    $("#productionLocationHeader").show(400);
}





