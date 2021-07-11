let rowTemplate = "<tr><td style='font-size:14px;color: deepskyblue'>No jobs to do</td></tr>";
let listDisplayed = false;
$('#check').click(function() {
    console.log("checked")
   if($('#workQueueList').attr("name").includes("showed")){
       $("#workQueueList").hide(400);
       $('#workQueueList').attr("name","hide")
   }
   else{
       if($('td[name="workNumber"]').text() == '' && listDisplayed == false){
           listDisplayed = true;
           $("#workNumberTable").append(
               rowTemplate)
       }
       $("#workQueueList").show(400);
       $('#workQueueList').attr("name","showed")
   }
});