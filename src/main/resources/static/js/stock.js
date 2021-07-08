let stockTR = $('tr[name="stockTR"]');
let statusTD = $('td[name="statusTD"]');
for(let i = 0; i < statusTD.length; i++){
    console.log(i + " " + $(statusTD[i]).children().last().attr("title"))

    if( $(statusTD[i]).children().last().attr("title").includes("on_hand")){
        $(statusTD[i]).children().last().attr("src","/images/on_hand30x30.png")
    }
    else if($(statusTD[i]).children().last().attr("title").includes("picking")){
        $(statusTD[i]).children().last().attr("src","/images/picking30x30.png")
        if($(statusTD[i]).children().last().attr("title").includes("production")){
            $(stockTR[i]).css("background-color", "blue")
        }
    }
    else if($(statusTD[i]).children().last().attr("title").includes("put_away")){
        $(statusTD[i]).children().last().attr("src","/images/putaway30x30.png")
        if($(statusTD[i]).children().last().attr("title").includes("production")){
            $(stockTR[i]).css("background-color", "blue")
        }
    }
    else if($(statusTD[i]).children().last().attr("title").includes("production")){
        $(stockTR[i]).css("background-color", "blue")
        $(statusTD[i]).children().last().attr("src","/images/30x30gear.gif")
    }
    else if($(statusTD[i]).children().last().attr("title").includes("reception")){
        $(statusTD[i]).children().last().attr("src","/images/on_reception30x30.png")
    }


}