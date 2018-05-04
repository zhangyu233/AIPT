// JavaScript Document
$(document).ready(function(e) {
	//accordian
	  $( function() {
    $( "#accordion" )
      .accordion({
        header: "> div > h3"
      })
      .sortable({
        axis: "y",
        handle: "h3",
        stop: function( event, ui ) {
          // IE doesn't register the blur when sorting
          // so trigger focusout handlers to remove .ui-state-focus
          ui.item.children( "h3" ).triggerHandler( "focusout" );
 
          // Refresh accordion to handle new order
          $( this ).accordion( "refresh" );
        }
      });
  } );
        //tab
        //匿名函数自调
        $(function(){
            //声明函数，参数三个：导航标题、当前选择项、当前标题显示内容
            function tabs(tabTit,on,tabCon){
                //找到所有标题并添加单机事件
                $(tabTit).children().click(function(){
                    //声明当前选择项
                    var index = $(tabTit).children().index(this);
                    //为当前选中项增加active，移除其兄弟元素的active
                    $(this).addClass(on).siblings().removeClass(on);
                    //选中项显示内容，未选中项隐藏内容
                    $(tabCon).children().eq(index).show().siblings().hide();
                });
            };
            tabs(".tab-hd","active",".tab-bd");
        });
		
		//div显示
		$(".clickme1").on("click",function(e) {
            $("#subchart").show("fade",500);
        });
		//div隐藏
        $(".return").on("click",function(e) {
            $("#subchart").hide("fade",500);
        });
		$(".flight").on( "overmouse", function(e) {
             $(this).css( "color", "red" );
        });

	});
		
