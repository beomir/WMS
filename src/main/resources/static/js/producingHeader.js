let allowToProduction = 2;

$( "#piecesToProduct" ).keyup(function() {
   let piecesToProduct = $("#piecesToProduct").val();
   let neededToProduceOne = $('td[name="neededToProduceOne"]');
   let neededToProduceAll = $('td[name="intermediate"]');
   let onStock = $('td[name="onStock"]');


   for(let i = 1;i < neededToProduceOne.length; i++){
      for(let j = 0; j < neededToProduceAll.length;j++){
         if( i - 1 == j){
         neededToProduceAll[j].innerHTML = neededToProduceOne[i].textContent * piecesToProduct
            for(let k = 1; k < onStock.length;k++) {
               if (k == j+1) {
                  if(onStock[k].textContent < neededToProduceOne[i].textContent * piecesToProduct){
                     $(neededToProduceAll[j]).css("background-color", "red")
                     allowToProduction = 1;
                  }
                  else{
                     $(neededToProduceAll[j]).css("background-color", "transparent")
                     allowToProduction = 0;
                  }
               }
            }
         }
      }
   }
});

function checkValidation() {
   console.log(allowToProduction)

   if(allowToProduction==1){
      alert("Not enough goods for production")
      returnToPreviousPage()
      return false;
   }
   if(allowToProduction==2){
      alert("No data entered about amount of good to produce")
      returnToPreviousPage()
      return false;
   }
}


function returnToPreviousPage() {
   window.history.forward(-1)
}
