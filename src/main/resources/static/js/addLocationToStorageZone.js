var counter = 0;
let selectZone = document.getElementById('selectZone');
let locations = document.getElementsByName('locationName');
let storageZones = document.getElementsByName('storageZone');
let locationsAssignedToStorage = document.getElementById('locationsAssignedToStorage');

selectZone.addEventListener("change", function () {
    counter = 0;
    locationsAssignedToStorage.innerHTML = "";
    for (var i = 0; i < storageZones.length -1; i++) {
        if (storageZones[i+1].textContent == selectZone.options[selectZone.selectedIndex].text) {
            locationsAssignedToStorage.append(locations[i].textContent);
            let br = document.createElement("br")
            locationsAssignedToStorage.append(br);
            counter++;
        }
    }
    let qtyOfLocInStorageZone = document.getElementById("qtyOfLocInStorageZone")
    qtyOfLocInStorageZone.innerHTML = counter;
})


//switching location type
let rackLocation = document.getElementById("rackLocation")
let doorLocation = document.getElementById("doorLocation")
let floorLocation = document.getElementById("floorLocation")
let select = document.getElementById('selectId')
rackLocation.classList.add("d-none")
floorLocation.classList.add("d-none")


select.addEventListener("change", function () {
    if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL") {
        rackLocation.classList.add("d-none")
        floorLocation.classList.add("d-none")
        doorLocation.classList.remove("d-none")


        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackSecondSepTo").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackThirdSepTo").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        $("#rackFourthSepTo").removeAttr('required').val("")


        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        $("#floorSecondSepTo").removeAttr('required').val("")


        doorFirstSep.setAttribute("required", "");
        doorSecondSep.setAttribute("required", "");
        doorThirdSep.setAttribute("required", "");
        doorThirdSepTo.setAttribute("required", "");



    } else if(document.getElementById('selectId').value == "PRL" || document.getElementById('selectId').value == "RRL"){
        rackLocation.classList.remove("d-none")
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


        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        $("#doorThirdSepTo").removeAttr('required').val("")

    }
    else{
        rackLocation.classList.add("d-none")
        doorLocation.classList.add("d-none")
        floorLocation.classList.remove("d-none")

        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        $("#doorThirdSepTo").removeAttr('required').val("")

        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackSecondSepTo").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackThirdSepTo").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        $("#rackFourthSepTo").removeAttr('required').val("")

        floorFirstSep.setAttribute("required", "");
        floorSecondSep.setAttribute("required", "");
        floorSecondSepTo.setAttribute("required", "");
    }
})

const hide = document.getElementById("assignedLocationsList")

hide.classList.add("d-none")

$('#assignedLocations').click(function() {
    if ($(this).is(':checked')) {
        $("#assignedLocationsList").show(400);
    } else {
        $("#assignedLocationsList").hide(400);
        console.log("Checkbox is unchecked.")
    }
});



function checkLocationsBeforeSubmit(){
    let currentLocation;
    //rack
    var rackFirstSep = document.getElementById("rackFirstSep").value
    var rackSecondSep = document.getElementById("rackSecondSep").value
    var rackSecondSepTo = document.getElementById("rackSecondSepTo").value
    var rackThirdSep = document.getElementById("rackThirdSep").value
    var rackThirdSepTo = document.getElementById("rackThirdSepTo").value
    var rackFourthSep = document.getElementById("rackFourthSep").value
    var rackFourthSepTo = document.getElementById("rackFourthSepTo").value
    var rackLoc;

    let firstRangeRack = parseInt(document.getElementById("rackSecondSepTo").value) - parseInt(document.getElementById("rackSecondSep").value) +1
    let secondRangeRack =  parseInt(document.getElementById("rackThirdSepTo").value) - parseInt(document.getElementById("rackThirdSep").value) +1
    let thirdRangeRack = parseInt(document.getElementById("rackFourthSepTo").value) - parseInt(document.getElementById("rackFourthSep").value) +1
    firstRangeRack = firstRangeRack || 0;
    secondRangeRack = secondRangeRack || 0;
    thirdRangeRack = thirdRangeRack || 0;

    let sumOfRackLocationToCreate = thirdRangeRack * secondRangeRack * firstRangeRack
    if(firstRangeRack<0 || firstRangeRack == null){
        sumOfRackLocationToCreate = 0;
    }
    if(secondRangeRack<0 || secondRangeRack == null){
        sumOfRackLocationToCreate = 0;
    }
    if(thirdRangeRack<1 || thirdRangeRack == null){
        sumOfRackLocationToCreate = 0;
    }

    //doorLocations
    var doorFirstSep = document.getElementById("doorFirstSep").value
    var doorSecondSep = document.getElementById("doorSecondSep").value
    var doorThirdSep = document.getElementById("doorThirdSep").value
    var doorThirdSepTo = document.getElementById("doorThirdSepTo").value
    var doorLoc;

    let firstRangeDoor = parseInt(document.getElementById("doorThirdSepTo").value) - parseInt(document.getElementById("doorThirdSep").value) +1
    firstRangeDoor = firstRangeDoor || 0;
    if(firstRangeDoor<0){
        firstRangeDoor = 0;
    }

    //floorLocations
    var floorFirstSep = document.getElementById("floorFirstSep").value
    var floorSecondSep = document.getElementById("floorSecondSep").value
    var floorSecondSepTo = document.getElementById("floorSecondSepTo").value
    var floorLoc;

    let firstRangeFloor = parseInt(document.getElementById("floorSecondSepTo").value) - parseInt(document.getElementById("floorSecondSep").value) + 1
    firstRangeFloor = firstRangeFloor || 0;
    if(firstRangeFloor<0){
        firstRangeFloor = 0;
    }

    counter = 0;
    let m = 0;
    for (let i = 0; i < storageZones.length -1; i++) {
        currentLocation = locations[i].textContent;
        console.log("wartosc dla i: " + i)
        if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL"){
            for(let j = doorThirdSep; j <= doorThirdSepTo; j++){
                console.log("wartosc dla j: " + j)
                m = j.toString();
                doorLoc = doorFirstSep + doorSecondSep + j.toString().padStart(2,"0")
                console.log("doorLoc: " + doorLoc)
                if(doorLoc == currentLocation){
                    counter++;
                }
            }
        }
        else if(document.getElementById('selectId').value == "PFL"){
            for(let i = floorSecondSep; i <= floorSecondSepTo; i++){
                floorLoc = floorFirstSep + i.toString().padStart(8,"0")
                if(floorLoc == currentLocation){
                    counter++;
                }
            }
        }
        else{
            for(let i = rackSecondSep; i<= rackSecondSepTo; i++){
                let rackNumber = i.toString().padStart(3,"0")
                let rackHeight;
                let locationNbr;
                for(let j = rackThirdSep; j <= rackThirdSepTo; i++){
                    rackHeight = j.toString().padStart(2,"0")
                    for(let k = rackFourthSep; k <= rackFourthSepTo; i++){
                        locationNbr = k.toString().padStart(3,"0")
                        rackLoc = rackFirstSep + rackNumber + rackHeight + locationNbr;
                        if(rackLoc == currentLocation){
                            counter++;
                        }
                    }
                }
            }
        }
    }
    if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL"){
        if(counter!=firstRangeDoor){
            console.log("First range Door: " +firstRangeDoor)
            console.log("counter: " + counter)
            alert("Requested door locations are out of bound existed locations scope")
            returnToPreviousPage();
            return false;
        }
    }
    else if(document.getElementById('selectId').value == "PFL"){
        if(counter!=firstRangeFloor){
            console.log("First range Floor: " +firstRangeFloor)
            console.log("counter: " + counter)
            alert("Requested floor locations are out of bound existed locations scope")
            returnToPreviousPage();
            return false;
        }
    }
    else{
        if(counter!=sumOfRackLocationToCreate){
            console.log("Racks: " +sumOfRackLocationToCreate)
            console.log("counter: " + counter)
            alert("Requested racks locations are out of bound existed locations scope")
            returnToPreviousPage();
            return false;
        }
    }
    console.log("First range Door: " +firstRangeDoor)
    console.log("First range Floor: " +firstRangeFloor)
    console.log("Racks: " +sumOfRackLocationToCreate)
    console.log("counter: " + counter)
}

function returnToPreviousPage() {
    window.history.forward(-1)
}