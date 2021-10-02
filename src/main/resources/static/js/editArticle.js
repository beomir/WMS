let productionArticleForIntermediate = $('#productionArticleForIntermediate').text()
let productionArticleTypeForIntermediate = $('#productionArticleTypeForIntermediate').text()
let intermediateQtyForProduction = $('#intermediateQtyForProduction').text()

let productionArticleConnectionForEdit = $('#productionArticleConnection')
let selectIdForEdit = $('#selectId')
let quantityForFinishedProductForEdit = $('#quantityForFinishedProduct')
let productionArticles = $("[name='productionArticles']");

let productionArticlesArray = [];
let finishProductStatus = true;
console.log("selected: " + $('#selectId option:selected').val())
console.log("selected: " + selectIdForEdit.val())

for (let i = 0; i < productionArticles.length;i++){
    productionArticlesArray.push({val: productionArticles[i].textContent, text: productionArticles[i].textContent})
}

$( "#productionArticleConnection" ).keyup(function() {
    if(selectIdForEdit.val() != "finish product"){
        for (let i = 0; i < productionArticles.length;i++){
            if(productionArticleConnectionForEdit.val() == productionArticles[i].textContent){
                console.log("exist")
                productionArticleConnectionForEdit.css("background-color", "LimeGreen")
                productionArticleConnectionForEdit.css("color", "white")
                productionArticleConnectionForEdit.css("border", "")
                productionArticleConnectionForEdit.css("font-weight", "")
                productionArticleConnectionForEdit.prop('title', 'This finish product is ok');
                finishProductStatus = true;
                break
            }
            else{
                console.log("not exist")
                productionArticleConnectionForEdit.css("background-color", "red")
                productionArticleConnectionForEdit.css("color", "gold")
                productionArticleConnectionForEdit.css("border", "2px dashed black")
                productionArticleConnectionForEdit.css("font-weight", "bold")
                productionArticleConnectionForEdit.prop('title', 'This finish product not exists');
                finishProductStatus = false;
            }
        }
    }
});

console.log(productionArticlesArray)
console.log("productionArticleForIntermediate: " + productionArticleForIntermediate)

////////// intermediate articles
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
if($('#qtyOfAssignedIntermediateToFinishProduct').text() >= 0 && productionArticleTypeForIntermediate != "intermediate"){
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

