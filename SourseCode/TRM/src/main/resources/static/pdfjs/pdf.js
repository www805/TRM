/**
 * 显示PDF
 * @param pdfdivid PDF所在div
 * @param pdfurl PDF的路径
 */
function showPDF(pdfdivid,pdfurl){

    $("#"+pdfdivid).empty();
    var pdfiframe='<iframe style="width:100%;height:97%;"src="/pdfjs/web/viewer.html?file='+encodeURIComponent(pdfurl)+'" id="pdfiframe"></iframe>';
    $("#pdfiframe").remove();
    $("#"+pdfdivid).append(pdfiframe);

}