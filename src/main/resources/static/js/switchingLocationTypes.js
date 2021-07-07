let rackLocation = document.getElementById("rackLocation")
let doorLocation = document.getElementById("doorLocation")
let floorLocation = document.getElementById("floorLocation")
let equipment = document.getElementById("equipment")

let hdControl = document.getElementById("hdControling")
let multiItem = document.getElementById("multiItem")
let select = document.getElementById('selectId')

let equipmentTitleCache =   document.getElementById("equipmentTitle")
let productionTitleCache = document.getElementById("productionTitle")


if(window.location.href.indexOf("formEditLocation") === - 1){
    rackLocation.classList.add("d-none")
    floorLocation.classList.add("d-none")
    equipment.classList.add("d-none")
    hdControl.classList.add("d-none")
    multiItem.classList.add("d-none")
    console.log("not editLocation");
}

if(window.location.href.indexOf("formEditLocation") > - 1){
    console.log("editLocation");
    checkLocationType();
}


select.addEventListener("change", function () {
    checkLocationType();
})

function checkLocationType(){
    if(document.getElementById('selectId').value == "SDL" || document.getElementById('selectId').value == "RDL") {

        $("#hdControling").hide(500);
        $("#multiItem").hide(500);

        racksHide();
        floorHide();
        equipmentHide();

        $("#doorLocation").show(500);
        doorFirstSep.setAttribute("required", "");
        doorSecondSep.setAttribute("required", "");
        doorThirdSep.setAttribute("required", "");
        doorThirdSepTo.setAttribute("required", "");

    } else if(document.getElementById('selectId').value == "PRL" || document.getElementById('selectId').value == "RRL"){
        floorHide();
        doorHide();
        equipmentHide();

        $("#rackLocation").show(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);
        rackFirstSep.setAttribute("required", "");
        rackSecondSep.setAttribute("required", "");
        rackSecondSepTo.setAttribute("required", "");
        rackThirdSep.setAttribute("required", "");
        rackThirdSepTo.setAttribute("required", "");
        rackFourthSep.setAttribute("required", "");
        rackFourthSepTo.setAttribute("required", "");



    }
    else if(document.getElementById('selectId').value == "EQL" || document.getElementById('selectId').value == "PPL"){
        floorHide();
        doorHide();
        racksHide();

        $("#equipment").show(500);
        $("#hdControling").show(500);
        $("#multiItem").show(500);

        equipmentFirstSep.setAttribute("required", "");
        equipmentSecondSep.setAttribute("required", "");

        if(document.getElementById('selectId').value == "EQL"){
            $("#equipmentTitle").remove();
            $("#productionTitle").remove();
            $('#equiProdName').append(equipmentTitleCache);
        }
        if(document.getElementById('selectId').value == "PPL"){
            $("#equipmentTitle").remove();
            $("#productionTitle").remove();
            $('#equiProdName').append(productionTitleCache);
        }


    }
    else{
        equipmentHide();
        doorHide();
        racksHide();

        $("#floorLocation").show(500);

        $("#hdControling").show(500);
        $("#multiItem").show(500);

        floorFirstSep.setAttribute("required", "");
        floorSecondSep.setAttribute("required", "");
        floorSecondSepTo.setAttribute("required", "");
    }
}

function racksHide(){
    $("#rackFirstSep").removeAttr('required').val("")
    $("#rackSecondSep").removeAttr('required').val("")
    $("#rackSecondSepTo").removeAttr('required').val("")
    $("#rackThirdSep").removeAttr('required').val("")
    $("#rackThirdSepTo").removeAttr('required').val("")
    $("#rackFourthSep").removeAttr('required').val("")
    $("#rackFourthSepTo").removeAttr('required').val("")
    rackLocationSimulation.innerHTML = "";
    $("#rackLocation").hide(500);
}

function floorHide(){
    $("#floorSecondSep").removeAttr('required').val("")
    $("#floorFirstSep").removeAttr('required').val("")
    $("#floorSecondSepTo").removeAttr('required').val("")
    floorLocationSimulation.innerHTML = "";
    $("#floorLocation").hide(500);
}

function equipmentHide(){
    $("#equipmentFirstSep").removeAttr('required').val("")
    $("#equipmentSecondSep").removeAttr('required').val("")
    $("#equipmentSecondSepTo").removeAttr('required').val("")
    equipmentSimulation.innerHTML = "";
    $("#equipment").hide(500);
}

function doorHide(){
    $("#doorFirstSep").removeAttr('required').val("")
    $("#doorSecondSep").removeAttr('required').val("")
    $("#doorThirdSep").removeAttr('required').val("")
    $("#doorThirdSepTo").removeAttr('required').val("")
    doorLocationSimulation.innerHTML = "";
    $("#doorLocation").hide(500);
}


