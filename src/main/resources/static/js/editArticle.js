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

console.log($('#qtyOfAssignedIntermediateToFinishProduct').text())
if($('#qtyOfAssignedIntermediateToFinishProduct').text() >= 0){
    $('#productionArticleConnection').val($('#qtyOfAssignedIntermediateToFinishProduct').text());
    if($('#qtyOfAssignedIntermediateToFinishProduct').text() > 0)
    $('#selectId').find('[value="intermediate"]').remove();

}


function changeOptionForIntermediate(){
    console.log($('#selectId').find(":selected").val())
    if($('#selectId').find(":selected").val() == "intermediate"){
        $("#productionArticleConnection").prop('required',true)
    }
    else{
        $("#productionArticleConnection").removeAttr('required').val("")
    }
}

