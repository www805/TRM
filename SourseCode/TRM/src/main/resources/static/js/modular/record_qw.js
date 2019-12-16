$(function () {
    $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#recorddetail_scrollhtml").closest(".layui-col-md7").width())});
    $(window).resize(function(){
        $('#recorddetail_webkit, #recorddetail_scrollhtml div:eq(0)').css({'width':($("#recorddetail_scrollhtml").closest(".layui-col-md7").width())});
    })
})