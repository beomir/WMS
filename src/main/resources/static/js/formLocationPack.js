
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
}
//doorLocations
let doorThirdSep = document.getElementById("doorThirdSep")
let doorThirdSepTo = document.getElementById("doorThirdSepTo")


let doorLocationSimulation = document.getElementById("doorLocationSimulation")

doorThirdSep.addEventListener("keyup", function () {
    let firstRangeDoor = parseInt(document.getElementById("doorThirdSepTo").value) - parseInt(document.getElementById("doorThirdSep").value) +1
    firstRangeDoor = firstRangeDoor || 0;
    if(firstRangeDoor<0){
        firstRangeDoor = 0;
    }

    doorLocationSimulation.innerHTML = firstRangeDoor;
});

doorThirdSepTo.addEventListener("keyup", function () {
    let firstRangeDoor = parseInt(document.getElementById("doorThirdSepTo").value) - parseInt(document.getElementById("doorThirdSep").value) +1
    firstRangeDoor = firstRangeDoor || 0;
    if(firstRangeDoor<0){
        firstRangeDoor = 0;
    }
    doorLocationSimulation.innerHTML = firstRangeDoor;
});


//floorLocations
let floorSecondSep = document.getElementById("floorSecondSep")
let floorSecondSepTo = document.getElementById("floorSecondSepTo")

let floorLocationSimulation = document.getElementById("floorLocationSimulation")

floorFirstSep.addEventListener("keyup", function () {
    let firstRangeFloor = parseInt(document.getElementById("floorSecondSepTo").value) - parseInt(document.getElementById("floorSecondSep").value) + 1
    firstRangeFloor = firstRangeFloor || 0;
    if(firstRangeFloor<0){
        firstRangeFloor = 0;
    }
    floorLocationSimulation.innerHTML = firstRangeFloor;
});
floorSecondSepTo.addEventListener("keyup", function () {
    let firstRangeFloor = parseInt(document.getElementById("floorSecondSepTo").value) - parseInt(document.getElementById("floorSecondSep").value) +1
    firstRangeFloor = firstRangeFloor || 0;
    if(firstRangeFloor<0){
        firstRangeFloor = 0;
    }
    floorLocationSimulation.innerHTML = firstRangeFloor;
});


//switching location type
let rackLocation = document.getElementById("rackLocation")
let doorLocation = document.getElementById("doorLocation")
let floorLocation = document.getElementById("floorLocation")
let hdControl = document.getElementById("hdControling")
let multiItem = document.getElementById("multiItem")
let select = document.getElementById('selectId')
rackLocation.classList.add("d-none")
floorLocation.classList.add("d-none")
hdControl.classList.add("d-none")
multiItem.classList.add("d-none")


select.addEventListener("change", function () {
    if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL") {

        $("#doorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#floorLocation").hide(500);
        $("#hdControling").hide(500);
        $("#multiItem").hide(500);

        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackSecondSepTo").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackThirdSepTo").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        $("#rackFourthSepTo").removeAttr('required').val("")
        rackLocationSimulation.innerHTML = "";

        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        $("#floorSecondSepTo").removeAttr('required').val("")
        floorLocationSimulation.innerHTML = "";

        doorFirstSep.setAttribute("required", "");
        doorSecondSep.setAttribute("required", "");
        doorThirdSep.setAttribute("required", "");
        doorThirdSepTo.setAttribute("required", "");



    } else if(document.getElementById('selectId').value == "PRL" || document.getElementById('selectId').value == "RRL"){
        $("#rackLocation").show(500);
        $("#doorLocation").hide(500);
        $("#floorLocation").hide(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);

        rackFirstSep.setAttribute("required", "");
        rackSecondSep.setAttribute("required", "");
        rackSecondSepTo.setAttribute("required", "");
        rackThirdSep.setAttribute("required", "");
        rackThirdSepTo.setAttribute("required", "");
        rackFourthSep.setAttribute("required", "");
        rackFourthSepTo.setAttribute("required", "");

        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        $("#floorSecondSepTo").removeAttr('required').val("")
        floorLocationSimulation.innerHTML = "";

        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        $("#doorThirdSepTo").removeAttr('required').val("")
        doorLocationSimulation.innerHTML = "";
    }
    else{
        $("#floorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#doorLocation").hide(500);
        $("#hdControling").show(500);
        $("#multiItem").show(500);


        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        $("#doorThirdSepTo").removeAttr('required').val("")
        doorLocationSimulation.innerHTML = "";

        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackSecondSepTo").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackThirdSepTo").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        $("#rackFourthSepTo").removeAttr('required').val("")
        rackLocationSimulation.innerHTML = "";

        floorFirstSep.setAttribute("required", "");
        floorSecondSep.setAttribute("required", "");
        floorSecondSepTo.setAttribute("required", "");
    }
})

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