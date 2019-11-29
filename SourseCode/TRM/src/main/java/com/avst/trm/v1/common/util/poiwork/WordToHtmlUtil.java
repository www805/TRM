package com.avst.trm.v1.common.util.poiwork;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLException;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

/**
 * Word转HTML
 */
public class WordToHtmlUtil {

    /**
     * 2007版本word转换成html
     */
    public static boolean Word2007ToHtml(String docxpath,String htmlpath){

        OutputStream out=null;
        InputStream in=null;
        XWPFDocument document=null;
        try {
            File f = new File(docxpath);
            if (!f.exists()) {
                LogUtil.intoLog(4,WordToHtmlUtil.class,"Word2007ToHtml Sorry File does not Exists!docxpath:"+docxpath);
            } else {
                if (f.getName().endsWith(".docx") || f.getName().endsWith(".DOCX")) {

                    // 1) 加载word文档生成 XWPFDocument对象
                    in = new FileInputStream(f);
                    document = new XWPFDocument(in);
                    // 2) 解析 XHTML配置 (这里设置IURIResolver来设置图片存放的目录)
                    String imgpath = "";
                    if(htmlpath.indexOf("/") > -1){
                        imgpath = htmlpath.substring(0,htmlpath.lastIndexOf("/")+1);
                    }else{
                        imgpath = htmlpath.substring(0,htmlpath.lastIndexOf("\\")+1);
                    }
                    File imageFolderFile = new File(imgpath);
                    XHTMLOptions options = XHTMLOptions.create().URIResolver(new FileURIResolver(imageFolderFile));
                    options.setExtractor(new FileImageExtractor(imageFolderFile));
                    options.setIgnoreStylesIfUnused(false);
                    options.setFragment(true);

                    // 3) 将 XWPFDocument转换成XHTML
                    out = new FileOutputStream(new File(htmlpath));
                    XHTMLConverter.getInstance().convert(document, out, options);
                    LogUtil.intoLog(1,WordToHtmlUtil.class,"docx转HTML成功"+htmlpath);
                    return true;
                } else {
                    LogUtil.intoLog(4,WordToHtmlUtil.class,"Word2007ToHtml Enter only MS Office 2007+ files,docxpath:"+docxpath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=out){
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (null!=document){
                    document.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (null!=in){
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.intoLog(4,WordToHtmlUtil.class,"Word2007ToHtml docx转HTML失败"+docxpath);
        return false;
    }

    /**
     * /**
     * 2003版本word转换成html
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static boolean Word2003ToHtml(String docpath,String htmlpath) {

        OutputStream outStream=null;
        InputStream input=null;
        HWPFDocument wordDocument=null;
        try {
            String imgpath = "";
            if(htmlpath.indexOf("/") > -1){
                imgpath = htmlpath.substring(0,htmlpath.lastIndexOf("/")+1);
            }else{
                imgpath = htmlpath.substring(0,htmlpath.lastIndexOf("\\")+1);
            }
            final String imgpath_=imgpath;

            input = new FileInputStream(new File(docpath));
            wordDocument = new HWPFDocument(input);

            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
            //设置图片存放的位置
            wordToHtmlConverter.setPicturesManager(new PicturesManager() {
                public String savePicture(byte[] content, PictureType pictureType, String suggestedName, float widthInches, float heightInches) {
                    File imgPath = new File(imgpath_);
                    if(!imgPath.exists()){//图片目录不存在则创建
                        imgPath.mkdirs();
                    }
                    File file = new File(imgpath_ + suggestedName);
                    OutputStream os=null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(content);

                    } catch (Exception e) {
                        e.printStackTrace();
                        LogUtil.intoLog(4,WordToHtmlUtil.class,"Word2003ToHtml doc转HTML时图片异常"+imgpath_ + suggestedName);
                    }finally {
                        try {
                            if(null!=os){
                                os.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    }
                    return imgpath_ + suggestedName;
                }
            });

            //解析word文档
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();

            File htmlFile = new File(htmlpath);
            outStream = new FileOutputStream(htmlFile);

            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(outStream);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);

            LogUtil.intoLog(1,WordToHtmlUtil.class,"doc转HTML成功"+htmlpath);
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
            transformerFactoryConfigurationError.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=outStream){
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (null!=wordDocument){
                    wordDocument.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (null!=input){
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LogUtil.intoLog(4,WordToHtmlUtil.class,"Word2003ToHtml doc转HTML失败"+docpath);
        return false;
    }

    /**
     * Word文档转HTML
     * @param wordpath Word全路径
     * @param htmlpath HTML全路径
     * @return
     */
    public static boolean wordToHtml(String wordpath,String htmlpath){

        if(StringUtils.isEmpty(htmlpath)){
            return false;
        }
        try {
            File file=new File(htmlpath);
            if(!file.exists()){
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (wordpath.endsWith(".docx") || wordpath.endsWith(".DOCX")) {
            return Word2007ToHtml(wordpath,htmlpath);
        }else{
            return Word2003ToHtml(wordpath,htmlpath);
        }
    }

    /**
     * 2007版本word转换成html
     */
    public static StringBuffer Word2007ToHtml_in(XWPFDocument document, HWPFDocument wordDocument, ByteArrayOutputStream baos, InputStream inputStream, int num){

        try {
            StringBuffer html=new StringBuffer();
            // 1) 加载word文档生成 XWPFDocument对象

            try {
                document = new XWPFDocument(inputStream);
            } catch (InvalidFormatException e) {
                if(num<2){//可以尝试一下
                    num++;
                    return Word2003ToHtml_in(document,wordDocument,baos,inputStream,num);//尝试用2003转一下
                }
                return null;
            }catch (POIXMLException e) {
                if (num < 2) {//可以尝试一下
                    num++;
                    return Word2003ToHtml_in(document,wordDocument,baos,inputStream,num);//尝试用2003转一下
                }
                return null;
            }

            // 也可以使用字符数组流获取解析的内容
            baos = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, baos, null);
            html.append( baos.toString());
            return html;

        }catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    /**
     * /**
     * 2003版本word转换成html
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static StringBuffer Word2003ToHtml_in(XWPFDocument document,HWPFDocument wordDocument,ByteArrayOutputStream baos,InputStream inputStream,int num) {

        try {

            StringBuffer html=new StringBuffer();

            try {
                wordDocument = new HWPFDocument(inputStream);
            } catch (OfficeXmlFileException e) {
                if(num<2){//可以尝试一下
                    num++;
                    return Word2007ToHtml_in(document,wordDocument,baos,inputStream,num);//尝试用2007转一下
                }
                return null;
            }finally {
            }
            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

            //解析word文档
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();

            baos = new ByteArrayOutputStream();

            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(baos);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            html.append(baos.toString());
            System.out.println("doc_in转HTML成功");
            return html;

        }catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Word2003ToHtml_in doc转HTML失败");
        return null;
    }

    /**
     * Word数据流转HTML数据流
     * @param inputStream Word数据流
     * @param name 文件名
     * @return
     */
    public static String wordToHtml_in2str(InputStream inputStream,String name){

        if(null==inputStream){
            return null;
        }
        if(null==name){
            return null;
        }

        XWPFDocument document=null;
        HWPFDocument wordDocument=null;
        ByteArrayOutputStream baos=null;

        try {
            StringBuffer rr=new StringBuffer();
            if (name.endsWith(".docx") || name.endsWith(".DOCX")) {

                rr=Word2007ToHtml_in(document,wordDocument,baos,inputStream,1);
                return setHtml_LineStyle(rr.toString(),2);
            }else{
                rr=Word2003ToHtml_in(document,wordDocument,baos,inputStream,1);
                return setHtml_LineStyle(rr.toString(),1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (null != wordDocument) {
                    wordDocument = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (null != document) {
                    document = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public static Map<String,String> getHtml_Css(org.jsoup.nodes.Document doc) {
        String[] styles = doc.head().select("style").html().split("\r\n");
        Map<String,String> css = new HashMap<String,String>();
        for(String style:styles)
        {
            String[] kv = style.split("\\{|\\}");
            if(null!=kv&&kv.length > 0){
                int k=0;
                while(k<kv.length-1){
                    css.put(kv[k], kv[k+1]);
                    k+=2;
                }
            }
        }
        return css;
    }


    /**
     *
     * @param htmlpath
     * @param type 1表示需要做style样式的补充，2不需要做style样式不改
     * @return
     */
    public static String setHtml_LineStyle(String htmlpath,int type) {

        System.out.println(htmlpath+"--htmlpath");
        org.jsoup.nodes.Document doc = Jsoup.parse(htmlpath);
        System.out.println(doc.html()+"--Jsouphtmlpath");
        String httml= null;
        try {
            Map<String,String> css = getHtml_Css(doc);
            Element body=doc.body();

            if(type==2){
                if(htmlpath.indexOf("div") > -1){
                    Elements divs =body.getElementsByTag("div");
                    String divhtml="";
                    for (Element div: divs) {
                        divhtml += div.html();
                        div.remove();
                    }
                    body.html(divhtml);
                }
            }else{
                for(String key:css.keySet())
                {
                    Elements elements=body.select(key);
                    String cssstyle="";
                    if(null==elements||elements.isEmpty()){
                        System.out.println("body.select(key).attr(\"style\") is null,key:"+key);
                    }else{
                        cssstyle=css.get(key)+elements.attr("style");
                        elements.attr("style","" );
                        elements.attr("style",cssstyle );

                    }
                }
            }


            httml = body.html();

            //把Word2003/2007默认的字体样式全改成宋体，这样做为了避免doc转HTML的布完整性
            if(httml.indexOf("font-family:Times New Roman")> -1){
                httml=httml.replaceAll("font-family:Times New Roman","font-family:宋体");
            }else if(httml.indexOf("font-family:'Times New Roman'")> -1){
                httml=httml.replaceAll("font-family:'Times New Roman'","font-family:'宋体'");
            }
            if(httml.indexOf("font-family:Calibri Light")> -1){
                httml=httml.replaceAll("font-family:Calibri Light","font-family:宋体");
            }else if(httml.indexOf("font-family:'Calibri Light'")> -1){
                httml=httml.replaceAll("font-family:'Calibri Light'","font-family:'宋体'");
            }

            //去掉A标签，改成span
            if(httml.indexOf("<a ")> -1){
                httml=httml.replaceAll("<a ","<span ").replaceAll("</a>","</span>");
            }
            if(httml.indexOf("<a>")> -1){
                httml=httml.replaceAll("<a>","<span>").replaceAll("</a>","</span>");
            }

            System.out.println(3+"--"+httml);

            httml=httml.replace(" ","  ");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc=null;
        }
        return httml;
    }


    /**
     * 2007版本word转换成html
     */
    public static StringBuffer Word2007ToHtml_in(InputStream inputStream){

        XWPFDocument document=null;
        ByteArrayOutputStream baos=null;
        try {
            StringBuffer html=new StringBuffer();
            // 1) 加载word文档生成 XWPFDocument对象

            document = new XWPFDocument(inputStream);

            // 也可以使用字符数组流获取解析的内容
            baos = new ByteArrayOutputStream();
            XHTMLConverter.getInstance().convert(document, baos, null);
            html.append( baos.toString());
            baos.close();
            return html;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=baos){
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (null!=document){
                    document.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * /**
     * 2003版本word转换成html
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public static StringBuffer Word2003ToHtml_in(InputStream inputStream) {

        HWPFDocument wordDocument=null;
        ByteArrayOutputStream baos=null;
        try {

            StringBuffer html=new StringBuffer();

            wordDocument = new HWPFDocument(inputStream);

            WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());

            //解析word文档
            wordToHtmlConverter.processDocument(wordDocument);
            Document htmlDocument = wordToHtmlConverter.getDocument();

            baos = new ByteArrayOutputStream();

            DOMSource domSource = new DOMSource(htmlDocument);
            StreamResult streamResult = new StreamResult(baos);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer serializer = factory.newTransformer();
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.METHOD, "html");
            serializer.transform(domSource, streamResult);
            html.append(baos.toString());
            LogUtil.intoLog(1,WordToHtmlUtil.class,"doc_in转HTML成功");
            return html;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
            transformerFactoryConfigurationError.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null!=baos){
                    baos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (null!=wordDocument){
                    wordDocument.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        LogUtil.intoLog(4,WordToHtmlUtil.class,"Word2003ToHtml_in doc转HTML失败");
        return null;
    }

    /**
     * Word数据流转HTML数据流
     * @param inputStream Word数据流
     * @param name 文件名
     * @return
     */
    public static StringBuffer wordToHtml_in2str2(InputStream inputStream,String name){

        if(null==inputStream){
            return null;
        }
        if(null==name){
            return null;
        }

        if (name.endsWith(".docx") || name.endsWith(".DOCX")) {
            return Word2007ToHtml_in(inputStream);
        }else{
            return Word2003ToHtml_in(inputStream);
        }
    }


    public static void main(String[] args) {

        long starttime=(new Date()).getTime();
        String htmlpath="C:\\Users\\Administrator\\Desktop\\北京市国安笔录系统-模板\\审讯记录.html";
        String docpath="C:\\Users\\Administrator\\Desktop\\北京市国安笔录系统-模板\\审讯记录.docx";
        String imgpath = htmlpath.substring(0,htmlpath.lastIndexOf("\\")+1);
        System.out.println(imgpath);

        Word2007ToHtml(docpath,htmlpath);
        System.out.println((new Date()).getTime()-starttime);
    }

}
