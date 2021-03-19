let counter = 0;
let selectZone = document.getElementById('selectZone');
let locations = document.getElementsByName('locationName');
var storageZones = document.getElementsByName('storageZone');
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
        $("#doorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#floorLocation").hide(500);


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

        $("#rackLocation").show(500);
        $("#doorLocation").hide(500);
        $("#floorLocation").hide(500);


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
        $("#floorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#doorLocation").hide(500);


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



    //rack
    let rackFirstSep = document.getElementById("rackFirstSep").value
    let rackSecondSep = document.getElementById("rackSecondSep").value
    let rackSecondSepTo = document.getElementById("rackSecondSepTo").value
    let rackThirdSep = document.getElementById("rackThirdSep").value
    let rackThirdSepTo = document.getElementById("rackThirdSepTo").value
    let rackFourthSep = document.getElementById("rackFourthSep").value
    let rackFourthSepTo = document.getElementById("rackFourthSepTo").value
    let rackLoc;

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
    let doorFirstSep = document.getElementById("doorFirstSep").value
    let doorSecondSep = document.getElementById("doorSecondSep").value
    let doorThirdSep = document.getElementById("doorThirdSep").value
    let doorThirdSepTo = document.getElementById("doorThirdSepTo").value
    let doorLoc;


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
    var currentLocation;
    var zoneForCurrentLocation;
    var differentStorageZonesLocation;
    var differentStorageZones = 0;
    var selectZone = document.getElementById('selectZone');
    var chosenStorageZone = selectZone.options[selectZone.selectedIndex].text;
    if(firstRangeFloor>0){
        for(let i = floorSecondSep; i <= floorSecondSepTo; i++){
            floorLoc = floorFirstSep + i.toString().padStart(8,"0")
            for(let j = 0; j < storageZones.length -1; j++){

                currentLocation = locations[j].textContent
                zoneForCurrentLocation = storageZones[j+1].textContent
                if(floorLoc == currentLocation){
                    counter++;
                    console.log("current location: " + currentLocation + ", zone: " + zoneForCurrentLocation + ", chosen zone in form: " + chosenStorageZone)
                    if(zoneForCurrentLocation == chosenStorageZone){
                        differentStorageZones++
                        differentStorageZonesLocation = currentLocation;
                    }
                }

            }
        }
        if(counter!=firstRangeFloor){
            console.log("First range Floor: " +firstRangeFloor)
            console.log("counter: " + counter)
            alert("Requested floor locations are out of bound existed locations scope")
            returnToPreviousPage();
            return false;
        }
    }
    else if(firstRangeDoor>0){
        for(let i = doorThirdSep; i <= doorThirdSepTo; i++){
            doorLoc = doorFirstSep + doorSecondSep + i.toString().padStart(2,"0")

            for (let j = 0; j < storageZones.length -1; j++){
                currentLocation = locations[j].textContent
                zoneForCurrentLocation = storageZones[j+1].textContent
                if(doorLoc == currentLocation){
                    counter++;
                    // console.log("current location: " + currentLocation + ", zone: " + zoneForCurrentLocation + ", chosen zone in form: " + chosenStorageZone)
                    if(zoneForCurrentLocation == chosenStorageZone){
                        differentStorageZones++
                        differentStorageZonesLocation = currentLocation;
                    }
                }
            }
        }
        if(counter!=firstRangeDoor){
                if(counter!=firstRangeDoor){
                    alert("Requested door locations are out of bound existed locations scope")
                    returnToPreviousPage();
                    return false;
                }
        }
    }
    else{
        for(let i = rackSecondSep; i<= rackSecondSepTo; i++) {
            let rackNumber = i.toString().padStart(3, "0")
            let rackHeight;
            let locationNbr;
            for (let j = rackThirdSep; j <= rackThirdSepTo; j++) {
                rackHeight = j.toString().padStart(2, "0")
                for (let k = rackFourthSep; k <= rackFourthSepTo; k++) {
                    locationNbr = k.toString().padStart(3, "0")
                    rackLoc = rackFirstSep + rackNumber + rackHeight + locationNbr;
                    for (var l = 0; l < storageZones.length - 1; l++) {
                        currentLocation = locations[l].textContent
                        zoneForCurrentLocation = storageZones[l+1].textContent
                        if (rackLoc == currentLocation) {
                            counter++;
                            console.log("current location: " + currentLocation + ", zone: " + zoneForCurrentLocation + ", chosen zone in form: " + chosenStorageZone)
                            if (zoneForCurrentLocation == chosenStorageZone) {
                                differentStorageZones++
                                differentStorageZonesLocation = currentLocation;
                            }
                        }
                    }
                }
            }
        }
        if(counter!=sumOfRackLocationToCreate){
                    console.log("Racks: " +sumOfRackLocationToCreate)
                    console.log("counter: " + counter)
                    alert("Requested racks locations are out of bound existed locations scope")
                    returnToPreviousPage();
                    return false;
        }
    }
    let locationsInDifferentStorageZone = counter - differentStorageZones;
    if(differentStorageZones>0){
        if(differentStorageZones==1){
            alert(differentStorageZones + " location was already in storage zone: " + chosenStorageZone + ", location " + differentStorageZonesLocation +   ", stayed in the same storage zone. Rest: " + locationsInDifferentStorageZone + ", locations changed storage zone. List of them you will find in transaction log")
        }
        else if(locationsInDifferentStorageZone==1){
            alert(differentStorageZones + " locations were already in storage zone: " + chosenStorageZone + ", only one location changed storage zone, information about it you will find in transaction log. More information about locations which not changed storage zone, you will find in issue log")
        }
        else{
            alert(differentStorageZones + " locations were already in storage zone: " + chosenStorageZone + ",rest " + locationsInDifferentStorageZone + " locations were changed, list of them you will find in transaction log. More information about locations which not changed storage zone, you will find in issue log")
        }
    }
    console.log("First range Door: " +firstRangeDoor)
    console.log("First range Floor: " +firstRangeFloor)
    console.log("Racks: " +sumOfRackLocationToCreate)
    console.log("counter: " + counter)
    console.log("differentStorageZones: " + differentStorageZones)

}

function returnToPreviousPage() {
    window.history.forward(-1)
}