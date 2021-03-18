
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
        rackLocation.classList.add("d-none")
        floorLocation.classList.add("d-none")
        hdControl.classList.add("d-none")
        multiItem.classList.add("d-none")
        doorLocation.classList.remove("d-none")


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
        rackLocation.classList.remove("d-none")
        hdControl.classList.remove("d-none")
        multiItem.classList.remove("d-none")
        doorLocation.classList.add("d-none")
        floorLocation.classList.add("d-none")

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
        rackLocation.classList.add("d-none")
        doorLocation.classList.add("d-none")
        floorLocation.classList.remove("d-none")
        hdControl.classList.remove("d-none")
        multiItem.classList.remove("d-none")

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