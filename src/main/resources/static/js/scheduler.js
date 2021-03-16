let hide = document.getElementById("howManyDaysBack")
let select = document.getElementById('selectid')
hide.classList.add("d-none")

select.addEventListener("change", function () {
    if(document.getElementById('selectid').value == "Stock") {
        hide.classList.add("d-none")
    } else {
        hide.classList.remove("d-none")
    }
})

if (document.querySelector('input[name="trigger"]')) {
    document.querySelectorAll('input[name="trigger"]').forEach((elem) => {
        elem.addEventListener("change", function(event) {
            let item = event.target.value;
            let inputElements = document.querySelectorAll(".triggerDays")
            if(item == "allDays"){
                for(let i = 0; inputElements[i]; ++i){
                    inputElements[i].checked = true;
                }
            }
            else if(item == "workDays"){
                for(let i = 0; inputElements[i]; ++i){
                    if(inputElements[i].value != "SUNDAY" && inputElements[i].value != "SATURDAY"){
                        inputElements[i].checked = true;
                    }
                    else if(inputElements[i].value == "SUNDAY" || inputElements[i].value == "SATURDAY"){
                        inputElements[i].checked = false;
                    }
                }
            }
            else if(item == "weekend"){
                for(let i = 0; inputElements[i]; ++i){
                    if(inputElements[i].value == "SUNDAY" || inputElements[i].value == "SATURDAY"){
                        inputElements[i].checked = true;
                    }
                    else if(inputElements[i].value != "SUNDAY" || inputElements[i].value != "SATURDAY"){
                        inputElements[i].checked = false;
                    }
                }
            }
        });
    });
}