$('#check').click(function() {
    console.log("checked")
   if($('#workQueueList').attr("name").includes("showed")){
       $("#workQueueList").hide(400);
       $('#workQueueList').attr("name","hide")
       console.log("includes showed")
   }
   else{
       $("#workQueueList").show(400);
       $('#workQueueList').attr("name","showed")
       console.log("not includes showed")
   }
});