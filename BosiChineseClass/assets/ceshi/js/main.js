;(function ($, window, document, undefined) {
    $(function(){
        init();
    });
    function init(){
    	$("#tabMainNav li").each(function(i){
    		$(this).click(function(){
        		$(".tabContent").hide();
        		$(".tabContent").eq(i).show()
    		});
    	});
    	$("#textExample ul li").each(function(i){
    		$(this).click(function(){
    			$("#textExample img").attr("src","images/textExample" + i + ".jpg");
    		}).hover(function(){$(this).css("background","#CCCCCC");},function(){$(this).css("background","transparent");});
    	});
    	$("#tabMainNav li").first().click();
    }
}(jQuery, window, document,undefined));