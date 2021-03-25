var doorLocationValue;
var notEnoughSpaceInLocation;
var notEnoughWeightInLocation;
var locationNotExistsValue;
var multiItemValueOnLocation;
var classMixingNotAvailable;
var nothingFilled = 1;

let locations = document.getElementsByName('locationName');
let storageZones = document.getElementsByName('storageZone');
let stockExists = document.getElementsByName('stockExists');
let articleNumberFromExisted = document.getElementsByName('articleNumberFromExisted');
let createNewPalletNumberInLocation = document.getElementById('createNewPalletNumberInLocation')
let createNewPalletNumberInLocationCheckbox = document.querySelector("input[name=createNewPalletNumberInLocationCheckbox]")
let potentialPalletNbr =  new Date().getFullYear() + "00000000000";

let hdNumber = document.getElementsByName('hdNumber')
let currentPallet;
let nextPallet = document.getElementById('nextPallet');
let hd_number = document.getElementById('hd_number');

let articleClasses = document.getElementsByName('articleClasses');
let articleClassesMixed = document.getElementsByName('articleClassesMixed');
let multiItem = document.getElementsByName('multiItem');

let articlesVolume = document.getElementsByName('articlesVolume');
let articlesWeight = document.getElementsByName('articlesWeight');
let locationType = document.getElementsByName('locationType');
let freeSpace = document.getElementsByName('freeSpace');

let freeWeight = document.getElementsByName('freeWeight');
let locationIsFull = 0;
let locationIsFullOfWeight = 0;
let stockArticleType = document.getElementsByName('stockArticleType');
let stockArticleNumber = document.getElementsByName('stockArticleNumber');

let articleInformation = document.getElementById('articleInformation');
let canBeMixed = 0;
let counter;
let doorLocations;

let locationOccupied;
let selectedArticleClass;
let selectedArticleClassesMixed;
let multiItemOnLocation;

let checkedArticleValue;
let currentLocationType;
let chosenArticle = document.getElementById('chosenArticle');
let message = document.getElementById('message');

let currentLocationFreeSpace = document.getElementById('currentLocationFreeSpace');
let currentLocationFreeWeight = document.getElementById('currentLocationFreeWeight');
let currentArticleType;
let currentArticle;

//input Pieces
$('#piecesQty').on('keyup', function (){
    checkLocationAvailability();
})

//change Article Number
chosenArticle.addEventListener("change", function() {
    checkLocationAvailability();
})

//input Location
$('#locationN').on('keyup', function () {
    checkLocationAvailability();
});

//checkbox createNewPalletNumberInLocation
createNewPalletNumberInLocationCheckbox.addEventListener('change', function (){
    if(this.checked){
        console.log("createNewPalletNumberInLocation checked")
        $("#hd_number").attr("readonly", false);
        $("#hd_number").attr("pattern", "[0-9]{18}");
        $('#hd_number').val(potentialPalletNbr);
        $('#hd_number').addClass("check");
    }
    else{
        console.log("createNewPalletNumberInLocation unchecked")
        $('#hd_number').val(currentPallet);
        $("#hd_number").attr("readonly", true);
    }
})

function checkLocationAvailability(){
    doorLocationValue = 0;
    notEnoughSpaceInLocation = 0;
    notEnoughWeightInLocation = 0;
    locationNotExistsValue = 0;
    multiItemValueOnLocation = 0;
    classMixingNotAvailable = 0;
    nothingFilled = 0;

    counter = 0
    locationOccupied = 0;
    multiItemOnLocation = 0;
    let piecesQty = document.getElementById('piecesQty').value;
    if (piecesQty==""){
        piecesQty = 0;
    }
    for(let i = 0; i < storageZones.length; i++) {
        if(locations[i].textContent == document.getElementById('locationN').value){
            let freeSpaceInLocation = freeSpace[i].textContent
            let freeWeightInLocation = freeWeight[i].textContent
            console.log("multiItem: " + multiItem[i].textContent);
            currentArticleType = stockArticleType[i].textContent;
            currentArticle = stockArticleNumber[i].textContent
            currentLocationType = locationType[i].textContent;
            currentPallet = hdNumber[i].textContent;
            console.log("currentPallet: " + currentPallet)
            if(multiItem[i].textContent == "false"){
                multiItemOnLocation = 1;
            }
            currentLocationFreeSpace.innerHTML = "Current free space in location: " + freeSpaceInLocation.toString() + " cm3";
            currentLocationFreeWeight.innerHTML = "Current free weight in location: " + freeWeightInLocation.toString() + " kg";
            for(let j = 0; j < articleNumberFromExisted.length; j++) {
                checkedArticleValue = chosenArticle.options[chosenArticle.selectedIndex].text
                if(articleNumberFromExisted[j].textContent == checkedArticleValue){
                    selectedArticleClass = articleClasses[j].textContent
                    selectedArticleClassesMixed = articleClassesMixed[j].textContent
                    if(currentLocationType == "RDL" || currentLocationType == "SDL"){
                        doorLocations = 1;
                    }
                    else {
                        doorLocations = 0;
                        let volumeOfArticleToAdd = piecesQty * articlesVolume[j].textContent;
                        let weightOfArticleToAdd = piecesQty * articlesWeight[j].textContent;
                        console.log("freeSpaceInLocation: " + freeSpaceInLocation)
                        console.log("volumeOfArticleToAdd: " + volumeOfArticleToAdd)
                        console.log("freeWeightInLocation: " + freeWeightInLocation)
                        console.log("weightOfArticleToAdd: " + weightOfArticleToAdd)
                        currentLocationFreeSpace.innerHTML = "Current free space in location: " + (freeSpaceInLocation - volumeOfArticleToAdd).toString() + " cm3";
                        currentLocationFreeWeight.innerHTML = "Current free weight in location: " + (freeWeightInLocation - weightOfArticleToAdd).toString() + " kg";
                        if(!selectedArticleClassesMixed.includes(currentArticleType)){
                            canBeMixed = 1;
                        }
                        else if(freeSpaceInLocation < volumeOfArticleToAdd){
                            locationIsFull = 1;
                            locationIsFullOfWeight = 0;
                            canBeMixed = 0;
                            $('#currentLocationFreeSpace').css('color', 'red');
                            $('#currentLocationFreeSpace').css('background-color', 'black');
                            $('#currentLocationFreeSpace').css('border', '2px solid');
                            $('#currentLocationFreeSpace').css('border-radius', '5px');
                            $('#currentLocationFreeWeight').css('color', 'red');
                            $('#currentLocationFreeWeight').css('background-color', 'black');
                            $('#currentLocationFreeWeight').css('border', '2px solid');
                            $('#currentLocationFreeWeight').css('border-radius', '5px');
                        }
                        else if(freeWeightInLocation < weightOfArticleToAdd){
                            locationIsFullOfWeight = 1;
                            locationIsFull = 0;
                            canBeMixed = 0;
                            $('#currentLocationFreeSpace').css('color', 'red');
                            $('#currentLocationFreeSpace').css('background-color', 'black');
                            $('#currentLocationFreeSpace').css('border', '2px solid');
                            $('#currentLocationFreeSpace').css('border-radius', '5px');
                            $('#currentLocationFreeWeight').css('color', 'red');
                            $('#currentLocationFreeWeight').css('background-color', 'black');
                            $('#currentLocationFreeWeight').css('border', '2px solid');
                            $('#currentLocationFreeWeight').css('border-radius', '5px');
                        }
                        else{
                            locationIsFull = 0;
                            locationIsFullOfWeight = 0;
                            canBeMixed = 0
                            $('#currentLocationFreeSpace').css('color', 'forestgreen');
                            $('#currentLocationFreeSpace').css('background-color', 'black');
                            $('#currentLocationFreeSpace').css('border', '2px solid');
                            $('#currentLocationFreeSpace').css('border-radius', '5px');
                            $('#currentLocationFreeWeight').css('color', 'forestgreen');
                            $('#currentLocationFreeWeight').css('background-color', 'black');
                            $('#currentLocationFreeWeight').css('border', '2px solid');
                            $('#currentLocationFreeWeight').css('border-radius', '5px');
                        }
                    }
                }
            }
            counter++;
            console.log("counter: " + counter)
            console.log("doorLocations: " + doorLocations)
            console.log("canBeMixed: " + canBeMixed)
            console.log("locationIsFull: " + locationIsFull)
            console.log("locationIsFullOfWeight: " + locationIsFullOfWeight)
            console.log("locationOccupied: " + locationOccupied)
            if(stockExists[i].textContent=="false"){
                locationOccupied++
                $("#ifLocationEmpty").hide(500);
                $("#createNewPalletNumberInLocation").hide(500);
                $("#createNewPalletNumberInLocationCheckbox").prop( "checked", false );
                $("#hd_number").attr("readonly", true);
                $('#hd_number').val(currentPallet);
            }
        }
    }
    if(doorLocations==1){
        message.innerHTML = "Location exists, but is not possible transfer stock directly to Door locations";
        $('#message').css('color', 'red');
        $('#message').css('background-color', 'black');
        $('#message').css('border', '2px solid');
        $('#message').css('border-radius', '5px');
        currentLocationFreeSpace.innerHTML ="";
        $('#currentLocationFreeSpace').css('color', 'transparent');
        $('#currentLocationFreeSpace').css('background-color', 'transparent');
        currentLocationFreeWeight.innerHTML ="";
        $('#currentLocationFreeWeight').css('color', 'transparent');
        $('#currentLocationFreeWeight').css('background-color', 'transparent');
        articleInformation.innerHTML = "Class: " + selectedArticleClass;
        $('#articleInformation').css('color', 'green');
        $('#articleInformation').css('background-color', 'black');
        $('#articleInformation').css('border', '2px solid');
        $('#articleInformation').css('border-radius', '5px');
        doorLocationValue = 1;
    }
    else if(locationIsFull==1){
        message.innerHTML = "You can not transfer this article with this quantity. Location have not enough space for it";
        $('#message').css('color', 'red');
        $('#message').css('background-color', 'black');
        $('#message').css('border', '2px solid');
        $('#message').css('border-radius', '5px');
        articleInformation.innerHTML = "Class: " + selectedArticleClass;
        $('#articleInformation').css('color', 'green');
        $('#articleInformation').css('background-color', 'black');
        $('#articleInformation').css('border', '2px solid');
        $('#articleInformation').css('border-radius', '5px');
        notEnoughSpaceInLocation = 1;
    }
    else if(locationIsFullOfWeight==1){
        message.innerHTML = "You can not add this article with this quantity. Location would be overweight";
        $('#message').css('color', 'red');
        $('#message').css('background-color', 'black');
        $('#message').css('border', '2px solid');
        $('#message').css('border-radius', '5px');
        articleInformation.innerHTML = "Class: " + selectedArticleClass;
        $('#articleInformation').css('color', 'green');
        $('#articleInformation').css('background-color', 'black');
        $('#articleInformation').css('border', '2px solid');
        $('#articleInformation').css('border-radius', '5px');
        notEnoughWeightInLocation = 1;
    }
    else if(counter == 0){
        message.innerHTML = "Location not exists!";
        $('#message').css('color', 'red');
        $('#message').css('background-color', 'black');
        $('#message').css('border', '2px solid');
        $('#message').css('border-radius', '5px');
        currentLocationFreeSpace.innerHTML ="";
        $('#currentLocationFreeSpace').css('color', 'transparent');
        $('#currentLocationFreeSpace').css('background-color', 'transparent');
        articleInformation.innerHTML = "Class: " + selectedArticleClass;
        currentLocationFreeWeight.innerHTML ="";
        $('#currentLocationFreeWeight').css('color', 'transparent');
        $('#currentLocationFreeWeight').css('background-color', 'transparent');
        $('#articleInformation').css('color', 'green');
        $('#articleInformation').css('background-color', 'black');
        $('#articleInformation').css('border', '2px solid');
        $('#articleInformation').css('border-radius', '5px');
        locationNotExistsValue = 1;
        $("#ifLocationEmpty").hide(500);
        $("#createNewPalletNumberInLocation").hide(500);
        $("#createNewPalletNumberInLocationCheckbox").prop( "checked", false );
        $("#hd_number").attr("readonly", true);
        $('#hd_number').val(currentPallet);

    }
    else if(locationOccupied>0){
        if(multiItemOnLocation==1){
            message.innerHTML = "Can't mix article in this location. Check multiItem value for this location";
            $('#message').css('color', 'red');
            $('#message').css('background-color', 'black');
            $('#message').css('border', '2px solid');
            $('#message').css('border-radius', '5px');
            multiItemValueOnLocation = 1;
        }
        else if(canBeMixed == 1){
            message.innerHTML = "Can't mix selected article ( class: " + selectedArticleClass + " ) in this location ( article: " + currentArticle  + ", class: " + currentArticleType + " )";
            $('#message').css('color', 'red');
            $('#message').css('background-color', 'black');
            $('#message').css('border', '2px solid');
            $('#message').css('border-radius', '5px');
            articleInformation.innerHTML = "Class: " + selectedArticleClass;
            $('#articleInformation').css('color', 'green');
            $('#articleInformation').css('background-color', 'black');
            $('#articleInformation').css('border', '2px solid');
            $('#articleInformation').css('border-radius', '5px');
            currentLocationFreeSpace.innerHTML ="";
            $('#currentLocationFreeSpace').css('color', 'transparent');
            $('#currentLocationFreeSpace').css('background-color', 'transparent');
            currentLocationFreeWeight.innerHTML ="";
            $('#currentLocationFreeWeight').css('color', 'transparent');
            $('#currentLocationFreeWeight').css('background-color', 'transparent');
            classMixingNotAvailable = 1;
        }
        else{
            message.innerHTML = "Location is occupied and stock can be transfer here";
            $('#message').css('color', 'forestgreen');
            $('#message').css('background-color', 'black');
            $('#message').css('border', '2px solid');
            $('#message').css('border-radius', '5px');
            articleInformation.innerHTML = "Class: " + selectedArticleClass;
            $('#articleInformation').css('color', 'green');
            $('#articleInformation').css('background-color', 'black');
            $('#articleInformation').css('border', '2px solid');
            $('#articleInformation').css('border-radius', '5px');
            $("#ifLocationEmpty").show(500);
            $("#createNewPalletNumberInLocation").show(500);
            $('#hd_number').val(currentPallet);
        }

    }
    else{
        message.innerHTML = "Location is empty and stock can be transfer here";
        $('#message').css('color', 'forestgreen');
        $('#message').css('background-color', 'black');
        $('#message').css('border', '2px solid');
        $('#message').css('border-radius', '5px');
        articleInformation.innerHTML = "Class: " + selectedArticleClass;
        $('#articleInformation').css('color', 'green');
        $('#articleInformation').css('background-color', 'black');
        $('#articleInformation').css('border', '2px solid');
        $('#articleInformation').css('border-radius', '5px');
        $("#ifLocationEmpty").show(500);
        $("#createNewPalletNumberInLocation").hide(500);
        $("#createNewPalletNumberInLocationCheckbox").prop( "checked", false );
        $("#hd_number").attr("readonly", true);
        $('#hd_number').val(nextPallet.textContent);
    }
}

function checkAllValidations(){
    console.log(doorLocationValue)
    console.log(notEnoughSpaceInLocation)
    console.log(notEnoughWeightInLocation)
    console.log(locationNotExistsValue)
    console.log(multiItemValueOnLocation)
    console.log(classMixingNotAvailable)
    let qty = document.getElementById("piecesQty").value;
    if(doorLocationValue==1){
        alert("Location exists, but is not possible transfer stock directly to Door locations")
        returnToPreviousPage();
        return false;
    }
    else if(notEnoughSpaceInLocation==1){
        alert("You can not add this article with this quantity. Location have not enough space for it")
        returnToPreviousPage();
        return false;
    }
    else if(notEnoughWeightInLocation==1){
        alert("You can not add this article with this quantity. Location would be overweight")
        returnToPreviousPage();
        return false;
    }
    else if(locationNotExistsValue==1){
        alert("Location not exists!")
        returnToPreviousPage();
        return false;
    }
    else if(multiItemValueOnLocation==1){
        alert("Can't mix article in this location. Check multiItem value for this location")
        returnToPreviousPage();
        return false;
    }
    else if(classMixingNotAvailable==1){
        alert("Can't mix selected article ( class: " + selectedArticleClass + " ) in this location ( article: " + currentArticle  + ", class: " + currentArticleType + " )")
        returnToPreviousPage();
        return false;
    }
    else if(nothingFilled==1){
        alert("No one field is filled")
        returnToPreviousPage();
        return false;
    }
    else if(qty==''){
        alert("Quantity field is empty")
        returnToPreviousPage();
        return false;
    }

}
function returnToPreviousPage() {
    window.history.forward(-1)
}