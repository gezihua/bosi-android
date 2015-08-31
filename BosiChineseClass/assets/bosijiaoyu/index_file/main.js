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
        var imgWidth = $("#imgExample").width();
        
        var containerWidth = (browserWidth - imgWidth - 45);
        
        if(containerWidth > 200){
            $("#mainContainer").css({"width":(0.95 * containerWidth) + "px","max-width":(0.95*containerWidth) + "px",});
        }
    }
}(jQuery, window, document,undefined));