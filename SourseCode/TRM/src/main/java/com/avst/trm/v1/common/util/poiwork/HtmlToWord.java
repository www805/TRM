package com.avst.trm.v1.common.util.poiwork;

import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.ReadLicense;
import com.avst.trm.v1.common.util.log.LogUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.List;

/**
 * html标签转word
 */
public class HtmlToWord {

    /**
     * 视图：页面
     * @param wordpath 导出后word地址
     * @param htmlcontent 需要转word的html内容
     * @return
     */
    public static boolean HtmlToWord(String wordpath, String htmlcontent){

        InputStream is=null;
        OutputStream os=null;
        POIFSFileSystem fs=null;
        if (null!=wordpath&&!wordpath.trim().equals("")){
            if(htmlcontent.indexOf("<html") > -1&&htmlcontent.indexOf("<body") > -1){//全页面导入
                System.out.println("全页面导入要处理");
                htmlcontent=delHtml_head(htmlcontent);//获取body的数据
            }
            //只有body里面的数据导入
//            String str = "<xml><w:WordDocument><w:View>Print</w:View><w:TrackMoves>false</w:TrackMoves><w:TrackFormatting/><w:ValidateAgainstSchemas/><w:SaveIfXMLInvalid>false</w:SaveIfXMLInvalid><w:IgnoreMixedContent>false</w:IgnoreMixedContent><w:AlwaysShowPlaceholderText>false</w:AlwaysShowPlaceholderText><w:DoNotPromoteQF/><w:LidThemeOther>EN-US</w:LidThemeOther><w:LidThemeAsian>ZH-CN</w:LidThemeAsian><w:LidThemeComplexScript>X-NONE</w:LidThemeComplexScript><w:Compatibility><w:BreakWrappedTables/><w:SnapToGridInCell/><w:WrapTextWithPunct/><w:UseAsianBreakRules/><w:DontGrowAutofit/><w:SplitPgBreakAndParaMark/><w:DontVertAlignCellWithSp/><w:DontBreakConstrainedForcedTables/><w:DontVertAlignInTxbx/><w:Word11KerningPairs/><w:CachedColBalance/><w:UseFELayout/></w:Compatibility><w:BrowserLevel>MicrosoftInternetExplorer4</w:BrowserLevel><m:mathPr><m:mathFont m:val=\"Cambria Math\"/><m:brkBin m:val=\"before\"/><m:brkBinSub m:val=\"--\"/><m:smallFrac m:val=\"off\"/><m:dispDef/><m:lMargin m:val=\"0\"/> <m:rMargin m:val=\"0\"/><m:defJc m:val=\"centerGroup\"/><m:wrapIndent m:val=\"1440\"/><m:intLim m:val=\"subSup\"/><m:naryLim m:val=\"undOvr\"/></m:mathPr></w:WordDocument></xml>";
//            String h = " <html xmlns:v='urn:schemas-microsoft-com:vml'xmlns:o='urn:schemas-microsoft-com:office:office'xmlns:w='urn:schemas-microsoft-com:office:word'xmlns:m='http://schemas.microsoft.com/office/2004/12/omml'xmlns='http://www.w3.org/TR/REC-html40'>";
            htmlcontent ="<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8' ></meta></head><body>"+htmlcontent+"</body> </html>";
            try {
                return  ConvertHtmlToDoc(htmlcontent,wordpath);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                IOUtils.closeQuietly(fs);
                IOUtils.closeQuietly(os);
                IOUtils.closeQuietly(is);
            }
        }
        System.out.println("HtmlToWord HTML转WORD失败"+wordpath);
        return false;
    }

    /**
     *
     * @param htmlpath
     * @return
     */
    public static String delHtml_head(String htmlpath) {

        org.jsoup.nodes.Document doc = Jsoup.parse(htmlpath);
        String httml= null;
        try {
            Element body=doc.body();
            httml = body.html();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc=null;
        }
        return httml;
    }

    public static boolean ConvertHtmlToDoc(String html, String savePath)
    {
        try
        {

            if(!getLicense()){// 验证License 若不验证则转化出的PDP文档会有水印产生
                System.out.println("word2pdf 验证License 失败");
            }

            Document doc = new Document();
            DocumentBuilder build = new DocumentBuilder(doc);
            build.insertHtml(html);
            doc.save(savePath);
            return true;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean getLicense() {

        boolean result = false;
        InputStream is=null;
        try {
            is = ReadLicense.getStringInputStream();
            com.aspose.words.License aposeLic = new com.aspose.words.License();
            aposeLic.setLicense(is);
            result = true;

        }
        catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null!=is){
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }

    public static void main(String[] args) {
        String content = "<div id=\"recorddetail\"><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \">被：责任重大。<br></p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"26918\" usertype=\"2\">被：周五能两天时间对吗？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"29401\" usertype=\"1\">审长：对。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"34221\" usertype=\"1\">审长：方法。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"40661\" usertype=\"1\">审长：因为。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"42181\" usertype=\"1\">审长：我看。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"51701\" usertype=\"1\">审长：占用的时间十分钟。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"54261\" usertype=\"1\">审长：就。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"56301\" usertype=\"1\">审长：在座的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"58741\" usertype=\"1\">审长：就是我需要。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"60541\" usertype=\"1\">审长：因为这边。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"62861\" usertype=\"1\">审长：有的东西。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"65921\" usertype=\"1\">审长：是这样。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"69941\" usertype=\"1\">审长：我大概用了这个。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"79081\" usertype=\"1\">审长：不能叫爸妈就是。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"82420\" usertype=\"2\">被：什么东西？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"86461\" usertype=\"1\">审长：以编制认证，有他这个发文章就是后面计划，就是我现在要改成面馆，基本上一模一样。对这东西，我大概有点思路，就是。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"86900\" usertype=\"2\">被：那么。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"103561\" usertype=\"1\">审长：ok我讲一下我。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"105457\" usertype=\"2\">被：就是说。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"107041\" usertype=\"1\">审长：无论如何。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"107417\" usertype=\"2\">被：说实话。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"110541\" usertype=\"1\">审长：那边就是这样。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"112841\" usertype=\"1\">审长：这个编辑器里面其实就是。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"116361\" usertype=\"1\">审长：喂听了一个。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"118521\" usertype=\"1\">审长：就是。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"119877\" usertype=\"2\">被：你们就是主任。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"121601\" usertype=\"1\">审长：是这么一个页面。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"124777\" usertype=\"2\">被：中央。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"126921\" usertype=\"1\">审长：我的理念就是可能就是里面就是真正的一些。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"131841\" usertype=\"1\">审长：文章文文章。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"133877\" usertype=\"2\">被：深化。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"135041\" usertype=\"1\">审长：我就是这样做的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"137421\" usertype=\"1\">审长：往里面，因为他不是分页，他一开始说是没有一开始。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"143041\" usertype=\"1\">审长：他们就是一个。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"147780\" usertype=\"2\">被：输入一行字。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"149041\" usertype=\"1\">审长：标签。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"150721\" usertype=\"1\">审长：是不是这样的就是现在这个我们。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"155061\" usertype=\"1\">审长：你也没有说。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"156441\" usertype=\"1\">审长：就是所有的都是一个标签的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"159621\" usertype=\"1\">审长：就说一句他一回车。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"167981\" usertype=\"1\">审长：他是这样的，他觉得回车就会生成另外一个标签。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"173961\" usertype=\"1\">审长：他这样做的话，主要就是医院的概念。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"177601\" usertype=\"1\">审长：他现在我要去做。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"180281\" usertype=\"1\">审长：因为在。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"180541\" usertype=\"2\">被：在你。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"181241\" usertype=\"1\">审长：应该这个外面。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"183701\" usertype=\"1\">审长：到了抗日。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"184041\" usertype=\"2\">被：第二位。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"189221\" usertype=\"1\">审长：估计l。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"192301\" usertype=\"1\">审长：好多人做那些就是质疑。实际上。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"196061\" usertype=\"1\">审长：最终的效果。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"201421\" usertype=\"1\">审长：就这种。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"202761\" usertype=\"1\">审长：夜夜的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"205361\" usertype=\"1\">审长：就是这么一个点。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"207741\" usertype=\"1\">审长：知道吧。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"209241\" usertype=\"1\">审长：这边的话这样做的话做些什么样式的调整，比如说底图，像这样的我可以给他，比如这样，这样设置是运营。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"211359\" usertype=\"2\">被：第二位。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"220141\" usertype=\"1\">审长：已经就是根据那个生产。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"223801\" usertype=\"1\">审长：这就是说每年的什么，那个编剧。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"228221\" usertype=\"1\">审长：然后那边比如外边圈没东西，是不是还包括一些什么那边效果效这些东西。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"235481\" usertype=\"1\">审长：对就可以通过这个电，为什么？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"238521\" usertype=\"1\">审长：这没问题？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"240541\" usertype=\"1\">审长：然后。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"241421\" usertype=\"1\">审长：我现在这块，我现在实际上没问题的就是说。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"246041\" usertype=\"1\">审长：既标签就是我。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"247473\" usertype=\"2\">被：我。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"248261\" usertype=\"1\">审长：然后每个点位我们固定的高度的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"251301\" usertype=\"1\">审长：那高度上应该差不多，如果没有问题的话，差不多就是18年。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"255841\" usertype=\"1\">审长：106年，他的实际可以可以使用的就是。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"260501\" usertype=\"1\">审长：结合实际可以使用可变的高度。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"264221\" usertype=\"1\">审长：那么。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"265021\" usertype=\"1\">审长：我先谁是我需要把。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"267361\" usertype=\"1\">审长：我们页面？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"268821\" usertype=\"1\">审长：导入的我前天上来险？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"272441\" usertype=\"1\">审长：那么我通过六张导进来之后。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"275741\" usertype=\"1\">审长：他会事业的，因为我们公司来进行掌握什么日夜对事业的话一夜情的那么我我通过我这个电位，我或许这种平常建议所有批准。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"288461\" usertype=\"1\">审长：我们一方面是高度就到这里。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"292581\" usertype=\"1\">审长：那我现在就是这样。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"295081\" usertype=\"1\">审长：我把必然会到之后发霉，从上到下一个房间。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"299621\" usertype=\"1\">审长：怎么算。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"301421\" usertype=\"1\">审长：你们的高度文件夹。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"304821\" usertype=\"1\">审长：就是比如说这个批发兼是二十一二十二十三一一外加给给各位听完以后价值当家长的高度。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"315261\" usertype=\"1\">审长：优化，大一的时候，我就在换届。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"320741\" usertype=\"1\">审长：他大姨妈有的时候换个地方美大，依然不放弃。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"325501\" usertype=\"1\">审长：这样就。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"326881\" usertype=\"1\">审长：但是一开始导致那时候我们。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"329841\" usertype=\"1\">审长：这样子。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"334001\" usertype=\"1\">审长：就就这样，这样的一个现在做新东西，就是说。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"339161\" usertype=\"1\">审长：像这样是可以到。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"341081\" usertype=\"1\">审长：一些编辑。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"343601\" usertype=\"1\">审长：比如说现在有三亿。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"347261\" usertype=\"1\">审长：你在这个医院里面，可能对以往的收入是不是？</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"351261\" usertype=\"1\">审长：刚刚投放的时候，因为你说因为批评编辑刚刚补换卡的时候。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"358781\" usertype=\"1\">审长：那么你就一一试一下到那如果说因为他是有限的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"364081\" usertype=\"1\">审长：后来自动换行的。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"365941\" usertype=\"1\">审长：深化文化。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"367821\" usertype=\"1\">审长：因为里面这个高度的，因为我把第二问高端固定固定死了。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"373881\" usertype=\"1\">审长：后来他为了内衬。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"376401\" usertype=\"1\">审长：因为你这里面批评性高度变化。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"379041\" usertype=\"1\">审长：不对称，里面高度肯定并。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"381621\" usertype=\"1\">审长：当我当我就给给一个。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"385221\" usertype=\"1\">审长：实施实施的一个讲一个检测。</p><p style=\"margin: 0px; padding: 7px 7px 7px 15px; -webkit-tap-highlight-color: rgba(0, 0, 0, 0); color: #000000; white-space: normal; border-bottom: 1px solid rgb(204, 204, 204); outline: none; font-family: \" starttime=\"390081\" usertype=\"1\">审长：检登发里面可能我输入的过程中总体的。</p></div>";
        byte b[] = content.getBytes();
        try {
            InputStream is = new ByteArrayInputStream(b);
            OutputStream os = new FileOutputStream("f:\\1.docx");
            POIFSFileSystem fs = new POIFSFileSystem();
            fs.createDocument(is, "WordDocument");
            fs.writeFilesystem(os);
            os.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
