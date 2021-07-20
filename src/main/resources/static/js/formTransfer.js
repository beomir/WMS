var doorLocationValue;
var equipmentLocationValue;
var productionLocationValue;
var notEnoughSpaceInLocation;
var notEnoughWeightInLocation;
var locationNotExistsValue;
var multiItemValueOnLocation;
var classMixingNotAvailable;
var nothingFilled = 1;
let splitPallet = document.getElementById('splitPallet');
let locationIsEmpty = 0;
let iterator;

let locations = document.getElementsByName('locationName');
let storageZones = document.getElementsByName('storageZone');
let stockExists = document.getElementsByName('stockExists');
let articleNumberFromExisted = document.getElementsByName('articleNumberFromExisted');

let originPallet = document.getElementById('originPallet')
let piecesQty = document.getElementById('piecesQty')
let originalPiecesQty = document.getElementById('originalPiecesQty').value
let originLocationName = document.getElementById('originLocationName')
let sameLocation;
let articleInLocation;
let originArticleNumber = document.getElementById('originArticleNumber')

let createNewPalletNumberInLocationCheckbox = document.querySelector("input[name=createNewPalletNumberInLocationCheckbox]")
let potentialPalletNbr =  new Date().getFullYear() + "00000000000";

let hdNumber = document.getElementsByName('hdNumber')
let currentPallet;
let companyForPalletToMerge = document.getElementsByName('companyName')
let currentCompany;
let originCompany = document.getElementById('originCompany');
let hd_number = document.getElementById('hd_number');
let sameCompany = 0;

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

let equipmentLocations;
let productionLocations;

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
let filledLocation;

let nearbyEmptyLocation = document.getElementById('nearbyEmptyLocation')
let foundEmptyNearbyLocationQty;
let nearbyEmptyLocationList = document.getElementById('nearbyEmptyLocationList')
let nearbyAvailableLocation = document.getElementById('nearbyAvailableLocation')
let foundAvailableNearbyLocationQty;
let nearbyAvailableLocationList = document.getElementById('nearbyAvailableLocationList')
let originLocationId;
let counterOfLocations;

nearbyAvailableLocation.addEventListener('change',function(){
    counterOfLocations = 0;
    if(this.checked){
        nearbyAvailableLocationList.innerHTML = "";
        foundAvailableNearbyLocationQty = 0;

        for(let q = 0; q < storageZones.length; q++){
            if(originLocationName.textContent == locations[q].textContent){
                originLocationId = q;
                break
            }
        }
        for(let i = originLocationId; i < storageZones.length; i++) {
            if (foundAvailableNearbyLocationQty == 2) {
                break;
            }
            iterator = i;
            locationOccupied = 0;
            multiItemOnLocation = 0;
            sameCompany = 0;
            sameLocation = 0;
            canBeMixed = 0;

            console.log("started")
                let freeSpaceInLocation = freeSpace[i].textContent
                let freeWeightInLocation = freeWeight[i].textContent
                currentArticleType = stockArticleType[i].textContent;
                currentArticle = stockArticleNumber[i].textContent
                currentLocationType = locationType[i].textContent;
                currentPallet = hdNumber[i].textContent;
                currentCompany = companyForPalletToMerge[i].textContent;
                filledLocation = document.getElementById('locationN').value
                if (multiItem[i].textContent == "false") {
                    multiItemOnLocation = 1;
                    console.log("multiitem")
                }
                if (originCompany.textContent == companyForPalletToMerge[i].textContent) {
                    sameCompany = 1;
                    console.log("company")
                }
                if (originLocationName.textContent == document.getElementById('locationN').value) {
                    sameLocation = 1
                    console.log("location")
                }
                for (let j = 0; j < articleNumberFromExisted.length; j++) {
                    checkedArticleValue = chosenArticle.options[chosenArticle.selectedIndex].text
                    articleInLocation = articleNumberFromExisted[j].textContent;
                    console.log("article")
                    if (articleNumberFromExisted[j].textContent == checkedArticleValue) {
                        selectedArticleClass = articleClasses[j].textContent
                        selectedArticleClassesMixed = articleClassesMixed[j].textContent
                        if (currentLocationType == "RDL" || currentLocationType == "SDL") {
                            doorLocations = 1;
                            console.log("doorLocations")
                        } else {
                            doorLocations = 0;
                            let volumeOfArticleToAdd = piecesQty * articlesVolume[j].textContent;
                            let weightOfArticleToAdd = piecesQty * articlesWeight[j].textContent;
                            if (!selectedArticleClassesMixed.includes(currentArticleType)) {
                                canBeMixed = 1;
                            } else if (freeSpaceInLocation < volumeOfArticleToAdd) {
                                locationIsFull = 1;
                                locationIsFullOfWeight = 0;
                                canBeMixed = 0;
                                console.log("freeSpaceInLocation")
                            } else if (freeWeightInLocation < weightOfArticleToAdd) {
                                locationIsFullOfWeight = 1;
                                locationIsFull = 0;
                                canBeMixed = 0;
                                console.log("freeWeightInLocation")
                            } else {
                                locationIsFull = 0;
                                locationIsFullOfWeight = 0;
                                canBeMixed = 0
                            }
                        }
                    }
                }
            console.log("multiItemOnLocation: " + multiItemOnLocation)
            console.log("stockExists[i].textContent: " + stockExists[i].textContent)
            console.log("checkedArticleValue: " + checkedArticleValue)
            console.log("currentArticle: " + currentArticle)
            console.log("originArticleNumber.textContent: " + originArticleNumber.textContent)
            console.log("canBeMixed: " + canBeMixed)
            console.log("locations[i].textContent: " + locations[i].textContent)
            if(stockExists[i].textContent=="false") {
                locationOccupied++
            }
            console.log("locationOccupied: " + locationOccupied)
            nearbyAvailablePartialOccupiedLocationsAdd();

        }

        for(let i = originLocationId; i >= 0; i--) {
            if (foundAvailableNearbyLocationQty == 2) {
                break;
            }
            iterator = i;
            locationOccupied = 0;
            multiItemOnLocation = 0;
            sameCompany = 0;
            sameLocation = 0;
            canBeMixed = 0;
            console.log("started2")
            let freeSpaceInLocation = freeSpace[i].textContent
            let freeWeightInLocation = freeWeight[i].textContent
            currentArticleType = stockArticleType[i].textContent;
            currentArticle = stockArticleNumber[i].textContent
            currentLocationType = locationType[i].textContent;
            currentPallet = hdNumber[i].textContent;
            currentCompany = companyForPalletToMerge[i].textContent;
            filledLocation = document.getElementById('locationN').value
            if (multiItem[i].textContent == "false") {
                multiItemOnLocation = 1;
            }
            if (originCompany.textContent == companyForPalletToMerge[i].textContent) {
                sameCompany = 1;
            }
            if (originLocationName.textContent == document.getElementById('locationN').value) {
                sameLocation = 1
            }
            for (let j = 0; j < articleNumberFromExisted.length; j++) {
                checkedArticleValue = chosenArticle.options[chosenArticle.selectedIndex].text
                articleInLocation = articleNumberFromExisted[j].textContent;
                if (articleNumberFromExisted[j].textContent == checkedArticleValue) {
                    selectedArticleClass = articleClasses[j].textContent
                    selectedArticleClassesMixed = articleClassesMixed[j].textContent
                    if (currentLocationType == "RDL" || currentLocationType == "SDL") {
                        doorLocations = 1;
                    } else {
                        doorLocations = 0;
                        let volumeOfArticleToAdd = piecesQty * articlesVolume[j].textContent;
                        let weightOfArticleToAdd = piecesQty * articlesWeight[j].textContent;
                        if (!selectedArticleClassesMixed.includes(currentArticleType)) {
                            canBeMixed = 1;
                        } else if (freeSpaceInLocation < volumeOfArticleToAdd) {
                            locationIsFull = 1;
                            locationIsFullOfWeight = 0;
                            canBeMixed = 0;
                        } else if (freeWeightInLocation < weightOfArticleToAdd) {
                            locationIsFullOfWeight = 1;
                            locationIsFull = 0;
                            canBeMixed = 0;
                        } else {
                            locationIsFull = 0;
                            locationIsFullOfWeight = 0;
                            canBeMixed = 0

                        }
                    }
                }
            }
            console.log("multiItemOnLocation: " + multiItemOnLocation)
            console.log("stockExists[i].textContent: " + stockExists[i].textContent)
            console.log("checkedArticleValue: " + checkedArticleValue)
            console.log("currentArticle: " + currentArticle)
            console.log("originArticleNumber.textContent: " + originArticleNumber.textContent)
            console.log("canBeMixed: " + canBeMixed)
            console.log("locations[i].textContent: " + locations[i].textContent)
            console.log("originLocationName.textContent: " + originLocationName.textContent)
            if (stockExists[i].textContent == "false") {
                locationOccupied++
            }
            console.log("locationOccupied: " + locationOccupied)
            nearbyAvailablePartialOccupiedLocationsAdd();
        }

        console.log("nearbyAvailableLocationList: " + nearbyAvailableLocationList)
        console.log("nearbyAvailableLocationList.textContent: " + nearbyAvailableLocationList.textContent)
        if(nearbyAvailableLocationList.textContent ==""){
            nearbyAvailableLocationList.innerHTML = "Can not find available, partially-occupied locations"
        }
        $("#nearbyAvailableLocationList").addClass("wallpaper")
        $("#nearbyAvailableLocationList").show(500);
        console.log("nearbyAvailableLocationList checkbox checked")
    }
    else{
        $("#nearbyAvailableLocationList").hide(500);
        console.log("nearbyAvailableLocationList checkbox unchecked")
    }
})

nearbyEmptyLocation.addEventListener('change',function(){
    if(this.checked) {
        nearbyEmptyLocationList.innerHTML = "";
        foundEmptyNearbyLocationQty = 0;
        for (let i = 0; i < storageZones.length; i++) {
            console.log("originLocationName " + originLocationName.textContent)
            console.log("locations[i].textContent " + locations[i].textContent)
            if (originLocationName.textContent == locations[i].textContent) {
                originLocationId = i;
                break;
            }
        }
        for (let j = originLocationId; j >= 0; j--) {
            console.log("stockExists[j].textContent: " + stockExists[j].textContent)
            console.log("loop down " + locations[j].textContent)
            if (stockExists[j].textContent == "true" && locationType[j].textContent != "RDL" && locationType[j].textContent != "SDL" && locationType[j].textContent != "PPL" && locationType[j].textContent != "EQL") {
                foundEmptyNearbyLocationQty++
                console.log("foundEmptyNearbyLocationQty " + foundEmptyNearbyLocationQty)
                nearbyEmptyLocationList.append(locations[j].textContent);
                let br = document.createElement("br")
                nearbyEmptyLocationList.append(br);
                if (foundEmptyNearbyLocationQty == 2) {
                    break;
                }
            }
        }
        for (let j = originLocationId; j < storageZones.length; j++) {
            console.log("stockExists[j].textContent: " + stockExists[j].textContent)
            console.log("loop up " + locations[j].textContent)
            if (stockExists[j].textContent == "true" && locationType[j].textContent != "RDL" && locationType[j].textContent != "SDL" && locationType[j].textContent != "PPL" && locationType[j].textContent != "EQL") {
                foundEmptyNearbyLocationQty++
                console.log("foundEmptyNearbyLocationQty " + foundEmptyNearbyLocationQty)
                nearbyEmptyLocationList.append(locations[j].textContent);
                let br = document.createElement("br")
                nearbyEmptyLocationList.append(br);
                if (foundEmptyNearbyLocationQty == 2) {
                    break;
                }
            }
        }
        console.log("nearbyEmptyLocationList: " + nearbyEmptyLocationList)
        console.log("nearbyEmptyLocationList.textContent: " + nearbyEmptyLocationList.textContent)
        if(nearbyEmptyLocationList.textContent==""){
            nearbyEmptyLocationList.innerHTML = "Can not find empty location"
        }
        $("#nearbyEmptyLocationList").addClass("wallpaper")
        $("#nearbyEmptyLocationList").show(500);
        console.log("nearbyEmptyLocation checkbox checked")
    }
    else{
        $("#nearbyEmptyLocationList").hide(500);
        console.log("nearbyEmptyLocation checkbox unchecked")
    }
})

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

piecesQty.addEventListener('keyup', function() {
    if(parseInt(document.getElementById('piecesQty').value) < parseInt(originalPiecesQty)){

        $('#hd_number').val(potentialPalletNbr);
        $("#hd_number").attr("readonly", false);
        $("#hd_number").attr("pattern", "[0-9]{18}");
        $('#hd_number').addClass("check");
        console.log("locationIsEmpty: " + locationIsEmpty)
        splitPallet.innerHTML = "Less pieces than original. Fill pallet number to split quantity from origin pallet"
        $('#splitPallet').css('color', 'red');
        $('#splitPallet').css('background-color', 'black');
        $('#splitPallet').css('border', '2px solid');
        $('#splitPallet').css('border-radius', '5px');
        console.log("checkedArticleValue + " + checkedArticleValue)
        console.log("currentArticle + " + currentArticle)
        if(sameCompany == 1 && locationIsEmpty == 0 && parseInt(document.getElementById('piecesQty').value) <= parseInt(originalPiecesQty)){
            console.log("dupa1")
            console.log("locationIsEmpty: " + locationIsEmpty)

            $("#createNewPalletNumberInLocation").show(500);
        }

    }
    else{
        // $("#ifLocationEmpty").hide(500);
        // $("#createNewPalletNumberInLocation").hide(500);
        $('#hd_number').val(originPallet.textContent);
        splitPallet.innerHTML = "";
        $('#splitPallet').css('color', 'transparent');
        $('#splitPallet').css('background-color', 'transparent');
        console.log("locationIsEmpty: " + locationIsEmpty)
        console.log("checkedArticleValue + " + checkedArticleValue)
        console.log("currentArticle + " + currentArticle)
        if(sameCompany == 1 && locationIsEmpty == 0 && parseInt(document.getElementById('piecesQty').value) <= parseInt(originalPiecesQty)){
            console.log("dupa2")
            console.log("locationIsEmpty: " + locationIsEmpty)
            $("#createNewPalletNumberInLocation").show(500);
        }
    }
})

//checkbox createNewPalletNumberInLocation
createNewPalletNumberInLocationCheckbox.addEventListener('change', function (){
    if(this.checked){
        console.log("createNewPalletNumberInLocation checked")
        $("#hd_number").attr("readonly", true);
        $("#hd_number").attr("pattern", "[0-9]{18}");
        $('#hd_number').val(currentPallet);
        $('#hd_number').addClass("check");
    }
    else{
        if(parseInt(document.getElementById('piecesQty').value) < parseInt(originalPiecesQty)){
            console.log("createNewPalletNumberInLocation unchecked")
            $('#hd_number').val(potentialPalletNbr);
            $("#hd_number").attr("readonly", false);
            $('#hd_number').addClass("check");
        }
        else if(parseInt(document.getElementById('piecesQty').value) == parseInt(originalPiecesQty)){
            console.log("createNewPalletNumberInLocation unchecked")
            $('#hd_number').val(originPallet.textContent);
            $("#hd_number").attr("readonly", false);
            $('#hd_number').addClass("check");
        }
    }
})

function checkLocationAvailability(){
    doorLocationValue = 0;
    equipmentLocationValue = 0;
    productionLocationValue = 0;
    notEnoughSpaceInLocation = 0;
    notEnoughWeightInLocation = 0;
    locationNotExistsValue = 0;
    multiItemValueOnLocation = 0;
    classMixingNotAvailable = 0;
    nothingFilled = 0;
    sameLocation = 0;
    sameCompany = 0;
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
            currentCompany = companyForPalletToMerge[i].textContent;

            filledLocation = document.getElementById('locationN').value
            console.log("currentPallet: " + currentPallet)
            console.log("currentCompany: " + currentCompany)
            console.log("riginCompany.value: " + originCompany.textContent)
            if(multiItem[i].textContent == "false"){
                multiItemOnLocation = 1;
            }
            if(originCompany.textContent == companyForPalletToMerge[i].textContent){
                sameCompany = 1;
            }
            if(originLocationName.textContent == document.getElementById('locationN').value){
                console.log("attempt make transfer to origin location")
                sameLocation = 1
            }
            console.log("sameCompany: " + sameCompany)
            currentLocationFreeSpace.innerHTML = "Current free space in location: " + freeSpaceInLocation.toString() + " cm3";
            currentLocationFreeWeight.innerHTML = "Current free weight in location: " + freeWeightInLocation.toString() + " kg";
            for(let j = 0; j < articleNumberFromExisted.length; j++) {
                checkedArticleValue = chosenArticle.options[chosenArticle.selectedIndex].text
                articleInLocation = articleNumberFromExisted[j].textContent;
                if(articleNumberFromExisted[j].textContent == checkedArticleValue){
                    selectedArticleClass = articleClasses[j].textContent
                    selectedArticleClassesMixed = articleClassesMixed[j].textContent
                    if(currentLocationType == "RDL" || currentLocationType == "SDL"){
                        doorLocations = 1;
                    }
                    else if(currentLocationType == "EQL"){
                        equipmentLocations = 1;
                    }
                    else if(currentLocationType == "PPL"){
                        productionLocations = 1;

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
            console.log("articleInLocation: " + originArticleNumber.textContent + " in location: " + originLocationName.textContent)
            console.log("currentArticle(ArticleInSelectedLocation: " + currentArticle + " in filled location: " + filledLocation)
            console.log("multiItemOnLocation: " + multiItemOnLocation )
            console.log("originArticleNumber: " + originArticleNumber.textContent )
            if(stockExists[i].textContent=="false"){
                locationOccupied++
                $("#ifLocationEmpty").hide(500);
                $("#createNewPalletNumberInLocation").hide(500);
                $("#createNewPalletNumberInLocationCheckbox").prop( "checked", false );
                $("#hd_number").attr("readonly", true);
                $('#hd_number').val(currentPallet);
                locationIsEmpty = 0;
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
        locationIsEmpty = 0;
    }
    else if(productionLocations ==1){
        message.innerHTML = "Location exists, but is not possible transfer stock directly to Production locations";
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
        productionLocationValue = 1;
        locationIsEmpty = 0;
    }
    else if(equipmentLocations==1){
        message.innerHTML = "Location exists, but is not possible transfer stock directly to Equipment locations";
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
        equipmentLocationValue = 1;
        locationIsEmpty = 0;
    }
    else if(sameLocation == 1){
        message.innerHTML = "You can not make a transfer to origin location";
        $('#message').css('color', 'red');
        $('#message').css('background-color', 'black');
        $('#message').css('border', '2px solid');
        $('#message').css('border-radius', '5px');
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
        locationIsEmpty = 0;
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
        locationIsEmpty = 0;
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
        locationIsEmpty = 0;

    }
    else if(locationOccupied>0){
        console.log("checkedArticleValue.textContent" + checkedArticleValue)
        console.log("currentArticle" + currentArticle)
        if(multiItemOnLocation==1 && checkedArticleValue != currentArticle){
            message.innerHTML = "Can't mix article in this location. Check multiItem value for this location";
            $('#message').css('color', 'red');
            $('#message').css('background-color', 'black');
            $('#message').css('border', '2px solid');
            $('#message').css('border-radius', '5px');
            multiItemValueOnLocation = 1;
            locationIsEmpty = 0;
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
            locationIsEmpty = 0;
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
            $('#hd_number').val(originPallet.textContent);
            locationIsEmpty = 0;
            if(sameCompany == 1 && locationIsEmpty == 0){
                $("#createNewPalletNumberInLocation").show(500);
                console.log("dupa3")
                console.log("locationIsEmpty: " + locationIsEmpty)
            }
            checkIfPartialTransfer()
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
        $('#hd_number').val(originPallet.textContent);
        locationIsEmpty = 1;
        checkIfPartialTransfer()
    }
    piecesQty.addEventListener('keyup', function() {
        if(parseInt(document.getElementById('piecesQty').value) < parseInt(originalPiecesQty)){

            $('#hd_number').val(potentialPalletNbr);
            $("#hd_number").attr("readonly", false);
            $("#hd_number").attr("pattern", "[0-9]{18}");
            $('#hd_number').addClass("check");
            console.log("locationIsEmpty: " + locationIsEmpty)
            splitPallet.innerHTML = "Less pieces than original. Fill pallet number to split quantity from origin pallet"
            $('#splitPallet').css('color', 'red');
            $('#splitPallet').css('background-color', 'black');
            $('#splitPallet').css('border', '2px solid');
            $('#splitPallet').css('border-radius', '5px');
            console.log("checkedArticleValue + " + checkedArticleValue)
            console.log("currentArticle + " + currentArticle)
            if(sameCompany == 1 && locationIsEmpty == 0 && parseInt(document.getElementById('piecesQty').value) <= parseInt(originalPiecesQty)){
                $("#createNewPalletNumberInLocation").show(500);
                console.log("dupa4")
                console.log("locationIsEmpty: " + locationIsEmpty)
            }

        }
        else{
            // $("#ifLocationEmpty").hide(500);
            // $("#createNewPalletNumberInLocation").hide(500);
            $('#hd_number').val(originPallet.textContent);
            splitPallet.innerHTML = "";
            $('#splitPallet').css('color', 'transparent');
            $('#splitPallet').css('background-color', 'transparent');
            console.log("locationIsEmpty: " + locationIsEmpty)
            console.log("checkedArticleValue + " + checkedArticleValue)
            console.log("currentArticle + " + currentArticle)
            if(sameCompany == 1 && locationIsEmpty == 0 && parseInt(document.getElementById('piecesQty').value) <= parseInt(originalPiecesQty)){
                $("#createNewPalletNumberInLocation").show(500);
                console.log("dupa5")
                console.log("locationIsEmpty: " + locationIsEmpty)
            }
        }
    })

//checkbox createNewPalletNumberInLocation
    createNewPalletNumberInLocationCheckbox.addEventListener('change', function (){
        if(this.checked){
            console.log("createNewPalletNumberInLocation checked")
            $("#hd_number").attr("readonly", true);
            $("#hd_number").attr("pattern", "[0-9]{18}");
            $('#hd_number').val(currentPallet);
            $('#hd_number').addClass("check");
        }
        else{
            if(parseInt(document.getElementById('piecesQty').value) < parseInt(originalPiecesQty)){
                console.log("createNewPalletNumberInLocation unchecked")
                $('#hd_number').val(potentialPalletNbr);
                $("#hd_number").attr("readonly", false);
                $('#hd_number').addClass("check");
            }
            else if(parseInt(document.getElementById('piecesQty').value) == parseInt(originalPiecesQty)){
                console.log("createNewPalletNumberInLocation unchecked")
                $('#hd_number').val(originPallet.textContent);
                $("#hd_number").attr("readonly", false);
                $('#hd_number').addClass("check");
            }
        }
    })
}

function checkIfPartialTransfer(){
    if(parseInt(document.getElementById('piecesQty').value) < parseInt(originalPiecesQty)) {
        $('#hd_number').val(potentialPalletNbr);
        $("#hd_number").attr("readonly", false);
        $("#hd_number").attr("pattern", "[0-9]{18}");
        $('#hd_number').addClass("check");
        console.log("locationIsEmpty: " + locationIsEmpty)
        splitPallet.innerHTML = "Less pieces than original. Fill pallet number to split quantity from origin pallet"
        $('#splitPallet').css('color', 'red');
        $('#splitPallet').css('background-color', 'black');
        $('#splitPallet').css('border', '2px solid');
        $('#splitPallet').css('border-radius', '5px');
        console.log("checkedArticleValue + " + checkedArticleValue)
        console.log("currentArticle + " + currentArticle)
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
    else if(productionLocationValue==1){
        alert("Location exists, but is not possible transfer stock directly to Production locations")
        returnToPreviousPage();
        return false;
    }
    else if(equipmentLocationValue==1){
        alert("Location exists, but is not possible transfer stock directly to Equipment locations")
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
    else if(qty==0){
        alert("Quantity can not be 0")
        returnToPreviousPage();
        return false;
    }
    else if(sameLocation==1){
        alert("You can not make a transfer to origin location")
        returnToPreviousPage();
        return false;
    }

}
function returnToPreviousPage() {
    window.history.forward(-1)
}

function nearbyAvailablePartialOccupiedLocationsAdd(){

    if (locationOccupied > 0 && canBeMixed == 0 && originLocationName.textContent != locations[iterator].textContent) {
        if (multiItemOnLocation == 1 && checkedArticleValue == currentArticle && locationType[iterator].textContent != "PPL" && locationType[iterator].textContent != "EQL") {
            foundAvailableNearbyLocationQty++
            counterOfLocations++;
            console.log("foundAvailableNearbyLocationQty " + foundAvailableNearbyLocationQty)
            console.log("locations[i].textContent: " + locations[iterator].textContent)
            console.log("nearbyAvailablePartialOccupiedLocationsSpan1: " + $('#nearbyAvailablePartialOccupiedLocationsSpan1').text())
            console.log("locationType[i].textContent: " + locationType[iterator].textContent)
            let dateSpan = document.createElement('span')
            let br = document.createElement("br")
            if($('#nearbyAvailablePartialOccupiedLocationsSpan1').text() != locations[iterator].textContent){
            document.getElementById('nearbyAvailableLocationList').appendChild(dateSpan);
            dateSpan.id = "nearbyAvailablePartialOccupiedLocationsSpan" + counterOfLocations
            document.getElementById('nearbyAvailablePartialOccupiedLocationsSpan' + counterOfLocations).append(locations[iterator].textContent);
            document.getElementById('nearbyAvailableLocationList').append(br);
            }
            // document.getElementById('nearbyAvailableLocationList').append(locations[iterator].textContent);
            // let br = document.createElement("br")
            // document.getElementById('nearbyAvailableLocationList').append(br);
        }
        else if(multiItemOnLocation == 0 && locationType[iterator].textContent != "PPL" && locationType[iterator].textContent != "EQL"){
            foundAvailableNearbyLocationQty++
            counterOfLocations++;
            console.log("foundAvailableNearbyLocationQty " + foundAvailableNearbyLocationQty)
            console.log("locations[i].textContent: " + locations[iterator].textContent)
            console.log("nearbyAvailablePartialOccupiedLocationsSpan1: " + $('#nearbyAvailablePartialOccupiedLocationsSpan1').text())
            console.log("locationType[i].textContent: " + locationType[iterator].textContent)
            // document.getElementById('nearbyAvailableLocationList').append(locations[iterator].textContent);
            // let br = document.createElement("br")
            // document.getElementById('nearbyAvailableLocationList').append(br);
            let dateSpan = document.createElement('span')
            let br = document.createElement("br")
            if($('#nearbyAvailablePartialOccupiedLocationsSpan1').text() != locations[iterator].textContent){
            document.getElementById('nearbyAvailableLocationList').appendChild(dateSpan);
            dateSpan.id = "nearbyAvailablePartialOccupiedLocationsSpan" + counterOfLocations
            document.getElementById('nearbyAvailablePartialOccupiedLocationsSpan' + counterOfLocations).append(locations[iterator].textContent);
            document.getElementById('nearbyAvailableLocationList').append(br);
            }
        }
    }
}