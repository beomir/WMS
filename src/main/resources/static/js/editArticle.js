let productionArticleForIntermediate = $('#productionArticleForIntermediate').text()
let productionArticleTypeForIntermediate = $('#productionArticleTypeForIntermediate').text()
let intermediateQtyForProduction = $('#intermediateQtyForProduction').text()

let productionArticleConnectionForEdit = $('#productionArticleConnection')
let selectIdForEdit = $('#selectId')
let quantityForFinishedProductForEdit = $('#quantityForFinishedProduct')

if(productionArticleTypeForIntermediate == "intermediate"){
    intermediateValues()
}

function intermediateValues(){
    productionArticleConnectionForEdit.val(productionArticleForIntermediate) ;
    quantityForFinishedProductForEdit.val(intermediateQtyForProduction);
    selectIdForEdit.val(productionArticleTypeForIntermediate).change();
    $('#selectId').find('[value="finish product"]').remove();
}


// function changeOptionForIntermediate(){
//     console.log($('#selectId').find(":selected").val())
//     if($('#selectId').find(":selected").val() == "intermediate"){
//         $('#productionArticleConnection').val($('#productionArticleForIntermediate').text()) ;
//         $('#quantityForFinishedProduct').val($('#intermediateQtyForProduction').text());
//     }
// }