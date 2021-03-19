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


//switching location type
let rackLocation = document.getElementById("rackLocation")
let doorLocation = document.getElementById("doorLocation")
let floorLocation = document.getElementById("floorLocation")
let select = document.getElementById('selectId')
let hdControl = document.getElementById("hdControling")
let multiItem = document.getElementById("multiItem")


select.addEventListener("change", function () {
    if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL") {
        $("#doorLocation").show(500);
        $("#rackLocation").hide(500);
        $("#floorLocation").hide(500);
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

        doorFirstSep.setAttribute("required", "");
        doorSecondSep.setAttribute("required", "");
        doorThirdSep.setAttribute("required", "");



    } else if(document.getElementById('selectId').value == "PRL" || document.getElementById('selectId').value == "RRL"){
        $("#rackLocation").show(500);
        $("#doorLocation").hide(500);
        $("#floorLocation").hide(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);


        rackSecondSep.setAttribute("required", "");
        rackFirstSep.setAttribute("required", "");
        rackThirdSep.setAttribute("required", "");
        rackFourthSep.setAttribute("required", "");

        $("#floorSecondSep").removeAttr('required').val("")
        $("#floorFirstSep").removeAttr('required').val("")
        floorLocationSimulation.innerHTML = "";

        $("#doorFirstSep").removeAttr('required').val("")
        $("#doorSecondSep").removeAttr('required').val("")
        $("#doorThirdSep").removeAttr('required').val("")
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
        doorLocationSimulation.innerHTML = "";

        $("#rackSecondSep").removeAttr('required').val("")
        $("#rackFirstSep").removeAttr('required').val("")
        $("#rackThirdSep").removeAttr('required').val("")
        $("#rackFourthSep").removeAttr('required').val("")
        rackLocationSimulation.innerHTML = "";

        floorFirstSep.setAttribute("required", "");
        floorSecondSep.setAttribute("required", "");
    }
})