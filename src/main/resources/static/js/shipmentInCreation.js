let firstValueOfLink = $('#link').attr('href');
let secondValueOfLink = firstValueOfLink + $('.shipmentNumber').first().text();
// console.log("firstValueOfLink: " + firstValueOfLink)
// console.log("secondValueOfLink: " + secondValueOfLink)
// console.log("$('.shipmentNumber').first().text(): " + $('.shipmentNumber').first().text())
if($('.shipmentNumber').text() == null || $('.shipmentNumber').text() == ''){
    $('#sectionLink').hide();
    $('#addOrCreate').text($('#openNewShipment').text())
    $('#addToShipment').remove()
    $('#openNewShipment').remove()
}
else{
    $('#link').attr('href',secondValueOfLink);
    $('#addOrCreate').text($('#addToShipment').text())
    $('#addToShipment').remove()
    $('#openNewShipment').remove()
    // console.log("setup a link for closeShipment")
}