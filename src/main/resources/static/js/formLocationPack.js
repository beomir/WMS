
let extremelyValueToCheck;
//rackLocations
let rackFirstSep = document.getElementById("rackFirstSep")

let rackSecondSep = document.getElementById("rackSecondSep")
let rackSecondSepTo = document.getElementById("rackSecondSepTo")
let rackThirdSep = document.getElementById("rackThirdSep")
let rackThirdSepTo = document.getElementById("rackThirdSepTo")
let rackFourthSep = document.getElementById("rackFourthSep")
let rackFourthSepTo = document.getElementById("rackFourthSepTo")

let rackLocationSimulation = document.getElementById("rackLocationSimulation")


rackSecondSep.addEventListener("keyup", function () {
    calculateRackLocations()
});
rackSecondSepTo.addEventListener("keyup", function () {
    calculateRackLocations()
});
rackThirdSep.addEventListener("keyup", function () {
    calculateRackLocations()
});
rackThirdSepTo.addEventListener("keyup", function () {
    calculateRackLocations()
});
rackFourthSep.addEventListener("keyup", function () {
    calculateRackLocations()
});
rackFourthSepTo.addEventListener("keyup", function () {
    calculateRackLocations()
});

function calculateRackLocations(){
    let firstRangeRack = parseInt(document.getElementById("rackSecondSepTo").value) - parseInt(document.getElementById("rackSecondSep").value) +1
    let secondRangeRack =  parseInt(document.getElementById("rackThirdSepTo").value) - parseInt(document.getElementById("rackThirdSep").value) +1
    let thirdRangeRack = parseInt(document.getElementById("rackFourthSepTo").value) - parseInt(document.getElementById("rackFourthSep").value) +1
    firstRangeRack = firstRangeRack || 0;
    secondRangeRack = secondRangeRack || 0;
    thirdRangeRack = thirdRangeRack || 0;

    let sumOfLocationToCreate = thirdRangeRack * secondRangeRack * firstRangeRack
    if(firstRangeRack<0 || firstRangeRack == null){
        sumOfLocationToCreate = 0;
    }
    if(secondRangeRack<0 || secondRangeRack == null){
        sumOfLocationToCreate = 0;
    }
    if(thirdRangeRack<1 || thirdRangeRack == null){
        sumOfLocationToCreate = 0;
    }

    rackLocationSimulation.innerHTML = sumOfLocationToCreate

    if(sumOfLocationToCreate > extremelyValueToCheck ){
        $("#rackLocationSimulation").css({'color' : 'red'})
        $("#maxLocationValue").css({'color' : 'red'})
    }
    else{
        $("#rackLocationSimulation").css({'color' : 'white'})
        $("#maxLocationValue").css({'color' : 'white'})
    }
}
//doorLocations
let doorThirdSep = document.getElementById("doorThirdSep")
let doorThirdSepTo = document.getElementById("doorThirdSepTo")

let doorLocationSimulation = document.getElementById("doorLocationSimulation")

doorFirstSep.addEventListener("keyup", function () {
    doorChanges();
});

doorSecondSep.addEventListener("keyup", function () {
    doorChanges();
});

doorThirdSep.addEventListener("keyup", function () {
    doorChanges();
});

doorThirdSepTo.addEventListener("keyup", function () {
    doorChanges();
});

function doorChanges(){
    let firstRangeDoor = parseInt(document.getElementById("doorThirdSepTo").value) - parseInt(document.getElementById("doorThirdSep").value) +1
    firstRangeDoor = firstRangeDoor || 0;
    if(firstRangeDoor<0){
        firstRangeDoor = 0;
    }
    doorLocationSimulation.innerHTML = firstRangeDoor;
    if(firstRangeDoor > extremelyValueToCheck ){
        $("#doorLocationSimulation").css({'color' : 'red'})
        $("#maxLocationValue").css({'color' : 'red'})
    }
    else{
        $("#doorLocationSimulation").css({'color' : 'white'})
        $("#maxLocationValue").css({'color' : 'white'})
    }
}

//floorLocations
let floorSecondSep = document.getElementById("floorSecondSep")
let floorSecondSepTo = document.getElementById("floorSecondSepTo")

let floorLocationSimulation = document.getElementById("floorLocationSimulation")

floorFirstSep.addEventListener("keyup", function () {
    floorChanges()
});
floorSecondSep.addEventListener("keyup", function () {
    floorChanges()
});
floorSecondSepTo.addEventListener("keyup", function () {
    floorChanges()
});

function floorChanges(){
    let firstRangeFloor = parseInt(document.getElementById("floorSecondSepTo").value) - parseInt(document.getElementById("floorSecondSep").value) + 1
    firstRangeFloor = firstRangeFloor || 0;
    if(firstRangeFloor<0){
        firstRangeFloor = 0;
    }
    floorLocationSimulation.innerHTML = firstRangeFloor;
    if(firstRangeFloor > extremelyValueToCheck ){
        $("#floorLocationSimulation").css({'color' : 'red'})
        $("#maxLocationValue").css({'color' : 'red'})
    }
    else{
        $("#floorLocationSimulation").css({'color' : 'white'})
        $("#maxLocationValue").css({'color' : 'white'})
    }
}

//Equipment
let equipmentSecondSep = document.getElementById("equipmentSecondSep")
let equipmentSecondSepTo = document.getElementById("equipmentSecondSepTo")

let equipmentSimulation = document.getElementById("equipmentSimulation")

equipmentFirstSep.addEventListener("keyup", function () {
    equipmentChanges();
});

equipmentSecondSep.addEventListener("keyup", function () {
    equipmentChanges();
});
equipmentSecondSepTo.addEventListener("keyup", function () {
    equipmentChanges();
});

function equipmentChanges(){
    let firstRangeEquipment = parseInt(document.getElementById("equipmentSecondSepTo").value) - parseInt(document.getElementById("equipmentSecondSep").value) + 1
    firstRangeEquipment = firstRangeEquipment || 0;
    if(firstRangeEquipment<0){
        firstRangeEquipment = 0;
    }
    equipmentSimulation.innerHTML = firstRangeEquipment;
    if(firstRangeEquipment > extremelyValueToCheck ){
        $("#equipmentSimulation").css({'color' : 'red'})
        $("#maxLocationValue").css({'color' : 'red'})
    }
    else{
        $("#equipmentSimulation").css({'color' : 'white'})
        $("#maxLocationValue").css({'color' : 'white'})
    }
}

$('#width,#height,#depth').on('keyup', function (){
    let volume = document.getElementById('volume');
    let width = document.getElementById('width').value;
    let height = document.getElementById('height').value;
    let depth = document.getElementById('depth').value;

    if(width != "" && width != null && height != null && height != "" && depth != null && depth != ""){
        volume.innerHTML = "Volume: " + (width * height * depth).toString() + " cm3";
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


let extremelyWarehouseValue = document.getElementsByName('extremelyWarehouseValue')
let extremelyValue = document.getElementsByName('extremelyValue')
let maxLocationValue = document.getElementById('maxLocationValue')

let chosenWarehouse = document.getElementById('chosenWarehouse');
console.log(chosenWarehouse.options[chosenWarehouse.selectedIndex].text)

chosenWarehouse.addEventListener('change', function() {
    for(let i = 0; i < extremelyValue.length; i++ ){
        console.log("extremelyWarehouseValue[i].textContent: " +extremelyWarehouseValue[i].textContent)
        console.log("chosenWarehouse.options[chosenWarehouse.selectedIndex].text: " +chosenWarehouse.options[chosenWarehouse.selectedIndex].text)
        if(extremelyWarehouseValue[i].textContent == chosenWarehouse.options[chosenWarehouse.selectedIndex].text){
            maxLocationValue.innerHTML = extremelyWarehouseValue[i].textContent + ":  " +  extremelyValue[i].textContent
            extremelyValueToCheck = extremelyValue[i].textContent
            console.log("done")
        }
        else{
            maxLocationValue.innerHTML = "not set for warehouse: " + chosenWarehouse.options[chosenWarehouse.selectedIndex].text;
        }
    }
    console.log(chosenWarehouse.options[chosenWarehouse.selectedIndex].text)
})

function extremelyMaxLocationsCreateValue(){
    for(let i = 0; i < extremelyValue.length; i++ ){
        console.log("extremelyWarehouseValue[i].textContent: " +extremelyWarehouseValue[i].textContent)
        console.log("chosenWarehouse.options[chosenWarehouse.selectedIndex].text: " +chosenWarehouse.options[chosenWarehouse.selectedIndex].text)
        if(extremelyWarehouseValue[i].textContent == chosenWarehouse.options[chosenWarehouse.selectedIndex].text){
            maxLocationValue.innerHTML =  extremelyWarehouseValue[i].textContent + ":  " +  extremelyValue[i].textContent
            extremelyValueToCheck = extremelyValue[i].textContent
            console.log("done")
        }
        else{
            maxLocationValue.innerHTML = "not set for warehouse: " + extremelyWarehouseValue[i].textContent;
        }
    }
    console.log(chosenWarehouse.options[chosenWarehouse.selectedIndex].text)
}




function checkValidation(){
    let width = document.getElementById('width').value;
    let height = document.getElementById('height').value;
    let depth = document.getElementById('depth').value;
    let weight = document.getElementById('weight').value;
    console.log("width: " + width)
    console.log("height: " + height)
    console.log("depth: " + depth)
    console.log("weight: " + weight)
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

