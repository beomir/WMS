let volume = document.getElementById('volume');
let width = document.getElementById('width').value;
let height = document.getElementById('height').value;
let depth = document.getElementById('depth').value;
volume.innerHTML = "Volume calculation by entered dimensions: " + (width * height * depth).toString() + " cm3";
$('#volume').css('color', 'green');
$('#volume').css('background-color', 'black');
$('#volume').css('border', '2px solid');
$('#volume').css('border-radius', '5px');

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

$('#width,#height,#depth,#weight').on('keyup', function (){
    let volume = document.getElementById('volume');
    let width = document.getElementById('width').value;
    let height = document.getElementById('height').value;
    let depth = document.getElementById('depth').value;
    let weight = document.getElementById('weight').value
    let occupiedSpaceInLocation = document.getElementById('occupiedSpaceInLocation').textContent;
    let occupiedWeightInLocation = document.getElementById('occupiedWeightInLocation').textContent;
    let calculatedVolume = width * height * depth;

    if(width != "" && width != null && height != null && height != "" && depth != null && depth != ""){
        volume.innerHTML = "Volume calculation by entered dimensions: " + (width * height * depth).toString() + " cm3";
        $('#volume').css('color', 'green');
        $('#volume').css('background-color', 'black');
        $('#volume').css('border', '2px solid');
        $('#volume').css('border-radius', '5px');
        if(calculatedVolume<parseInt(occupiedSpaceInLocation)){
            $('#informationAboutVolumeInLocation').css('color', 'red');
            if(parseInt(weight)<parseInt(occupiedWeightInLocation)){
                $('#informationAboutWeightInLocation').css('color', 'red');
            }
            else{
                $('#informationAboutWeightInLocation').css('color', 'green');
            }
        }
        else if(parseInt(weight)<parseInt(occupiedWeightInLocation)){
            $('#informationAboutWeightInLocation').css('color', 'red');
        }
        else if(calculatedVolume>=parseInt(occupiedSpaceInLocation)){
            $('#informationAboutVolumeInLocation').css('color', 'green');
            if(parseInt(weight)<parseInt(occupiedWeightInLocation)){
                $('#informationAboutWeightInLocation').css('color', 'red');
            }
            else{
                $('#informationAboutWeightInLocation').css('color', 'green');
            }
        }
        else{
            $('#informationAboutWeightInLocation').css('color', 'green');
        }
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
    let occupiedSpaceInLocation = document.getElementById('occupiedSpaceInLocation').textContent;
    let occupiedWeightInLocation = document.getElementById('occupiedWeightInLocation').textContent;
    let calculatedVolume = width * height * depth;

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
    else if(calculatedVolume<parseInt(occupiedSpaceInLocation)){
        alert("Entered volume must be bigger than sum of articles volume occupied location")
        returnToPreviousPage();
        return false;
    }
    else if(parseInt(weight)<parseInt(occupiedWeightInLocation)){
        alert("Entered weight must be bigger than sum of articles weight occupied location")
        returnToPreviousPage();
        return false;
    }

}

function returnToPreviousPage() {
    window.history.forward(-1)
}