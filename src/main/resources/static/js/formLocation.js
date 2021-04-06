
//rackLocations
let rackFirstSep = document.getElementById("rackFirstSep")
let rackSecondSep = document.getElementById("rackSecondSep")
let rackThirdSep = document.getElementById("rackThirdSep")
let rackFourthSep = document.getElementById("rackFourthSep")

let rackLocationSimulation = document.getElementById("rackLocationSimulation")


rackFirstSep.addEventListener("keyup", function () {
    rackLocationSimulation.innerHTML = document.getElementById("rackFirstSep").value + document.getElementById("rackSecondSep").value + document.getElementById("rackThirdSep").value + document.getElementById("rackFourthSep").value
});
rackSecondSep.addEventListener("keyup", function () {
    rackLocationSimulation.innerHTML = document.getElementById("rackFirstSep").value + document.getElementById("rackSecondSep").value + document.getElementById("rackThirdSep").value + document.getElementById("rackFourthSep").value
});
rackThirdSep.addEventListener("keyup", function () {
    rackLocationSimulation.innerHTML = document.getElementById("rackFirstSep").value + document.getElementById("rackSecondSep").value + document.getElementById("rackThirdSep").value + document.getElementById("rackFourthSep").value
});
rackFourthSep.addEventListener("keyup", function () {
    rackLocationSimulation.innerHTML = document.getElementById("rackFirstSep").value + document.getElementById("rackSecondSep").value + document.getElementById("rackThirdSep").value + document.getElementById("rackFourthSep").value
});

//doorLocations
let doorFirstSep = document.getElementById("doorFirstSep")
let doorSecondSep = document.getElementById("doorSecondSep")
let doorThirdSep = document.getElementById("doorThirdSep")


let doorLocationSimulation = document.getElementById("doorLocationSimulation")

doorFirstSep.addEventListener("keyup", function () {
    doorLocationSimulation.innerHTML = document.getElementById("doorFirstSep").value + document.getElementById("doorSecondSep").value + document.getElementById("doorThirdSep").value
});
doorSecondSep.addEventListener("keyup", function () {
    doorLocationSimulation.innerHTML = document.getElementById("doorFirstSep").value + document.getElementById("doorSecondSep").value + document.getElementById("doorThirdSep").value
});
doorThirdSep.addEventListener("keyup", function () {
    doorLocationSimulation.innerHTML = document.getElementById("doorFirstSep").value + document.getElementById("doorSecondSep").value + document.getElementById("doorThirdSep").value
});


//floorLocations
let floorFirstSep = document.getElementById("floorFirstSep")
let floorSecondSep = document.getElementById("floorSecondSep")

let floorLocationSimulation = document.getElementById("floorLocationSimulation")

floorFirstSep.addEventListener("keyup", function () {
    floorLocationSimulation.innerHTML = document.getElementById("floorFirstSep").value + document.getElementById("floorSecondSep").value
});
floorSecondSep.addEventListener("keyup", function () {
    floorLocationSimulation.innerHTML = document.getElementById("floorFirstSep").value + document.getElementById("floorSecondSep").value
});

//equipment
let equipmentFirstSep = document.getElementById("equipmentFirstSep")
let equipmentSecondSep = document.getElementById("equipmentSecondSep")

let equipmentSimulation = document.getElementById("equipmentSimulation")

equipmentFirstSep.addEventListener("keyup", function () {
    equipmentSimulation.innerHTML = document.getElementById("equipmentFirstSep").value + document.getElementById("equipmentSecondSep").value
});
equipmentSecondSep.addEventListener("keyup", function () {
    equipmentSimulation.innerHTML = document.getElementById("equipmentFirstSep").value + document.getElementById("equipmentSecondSep").value
});


//switching location type
let rackLocation = document.getElementById("rackLocation")
let doorLocation = document.getElementById("doorLocation")
let floorLocation = document.getElementById("floorLocation")
let equipment = document.getElementById("equipment")

let hdControl = document.getElementById("hdControling")
let multiItem = document.getElementById("multiItem")
let select = document.getElementById('selectId')
rackLocation.classList.add("d-none")
equipment.classList.add("d-none")
floorLocation.classList.add("d-none")
hdControl.classList.add("d-none")
multiItem.classList.add("d-none")


select.addEventListener("change", function () {
    if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL") {
        $("#doorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#floorLocation").hide(500);
        $("#equipment").hide(500);

        $("#hdControling").hide(500);
        $("#multiItem").hide(500);

        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        rackLocationSimulation.innerHTML = "";

        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        floorLocationSimulation.innerHTML = "";

        $("#equipmentFirstSep").removeAttr('required').val("")
        $("#equipmentSecondSep").removeAttr('required').val("")
        equipmentSimulation.innerHTML = "";

        doorFirstSep.setAttribute("required", "");
        doorSecondSep.setAttribute("required", "");
        doorThirdSep.setAttribute("required", "");



    } else if(document.getElementById('selectId').value == "PRL" || document.getElementById('selectId').value == "RRL"){
        $("#rackLocation").show(500);
        $("#doorLocation").hide(500);
        $("#floorLocation").hide(500);
        $("#equipment").hide(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);

        rackSecondSep.setAttribute("required", "");
        rackFirstSep.setAttribute("required", "");
        rackThirdSep.setAttribute("required", "");
        rackFourthSep.setAttribute("required", "");

        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        floorLocationSimulation.innerHTML = "";

        $("#equipmentFirstSep").removeAttr('required').val("")
        $("#equipmentSecondSep").removeAttr('required').val("")
        equipmentSimulation.innerHTML = "";

        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        doorLocationSimulation.innerHTML = "";
    }

    else if(document.getElementById('selectId').value == "EQL"){
        $("#rackLocation").hide(500);
        $("#doorLocation").hide(500);
        $("#floorLocation").hide(500);
        $("#equipment").show(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);

        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        rackLocationSimulation.innerHTML = "";

        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        floorLocationSimulation.innerHTML = "";

        equipmentFirstSep.setAttribute("required", "");
        equipmentSecondSep.setAttribute("required", "");

        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        doorLocationSimulation.innerHTML = "";
    }
    else{
        $("#floorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#doorLocation").hide(500);
        $("#equipment").hide(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);

        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
        doorLocationSimulation.innerHTML = "";

        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        rackLocationSimulation.innerHTML = "";

        $("#equipmentFirstSep").removeAttr('required').val("")
        $("#equipmentSecondSep").removeAttr('required').val("")
        equipmentSimulation.innerHTML = "";

        floorFirstSep.setAttribute("required", "");
        floorSecondSep.setAttribute("required", "");
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