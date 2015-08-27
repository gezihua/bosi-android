;(function ($, window, document, undefined) {
    $(function(){
        init();
    });
    function init(){
        setMainContainerWidth();
    	$("#mainContainer>nav>a").each(function(i){
    		$(this).click(function(){
        		$(".tabContent").hide();
        		$(".tabContent").eq(i).show()
    		});
    	});
    	$("#textExample nav a").each(function(i){
    		$(this).click(function(){
    			$("#textExample>div:visible").hide();
                $("#textExample>div").eq(i).show();
    		}).hover(function(){$(this).css("background","#CCCCCC");},function(){$(this).css("background","transparent");});
    	});
    	$("#mainContainer>nav>a").first().click();
        $("#textExample>nav>a").first().click();
        $(window).resize(function(){setMainContainerWidth();});
    }
    function setMainContainerWidth(){
        var minWidth = 568;
        var maxHeigth = 480;
        var scale = 854/480;

        var browserWidth = $(window).width();
		var browseHeight = $(window).height();
        var imgWidth = $("#imgExample").width();
		var imgHeight = $("#imgExample").height();
        
        var containerWidth = (browserWidth - imgWidth - 100);
		var containerHeight =(browseHeight - imgHeight - 150);
        
        if(containerWidth > 200){
            $("#mainContainer").css({"width":(0.95 * containerWidth) + "px","max-width":(0.95*containerWidth) + "px",});

            $(".ta>textarea").css({"width":(0.9 * containerWidth) + "px", "height":((0.9*containerHeight))+"px"});
        }
		
		  
    }
}(jQuery, window, document,undefined));