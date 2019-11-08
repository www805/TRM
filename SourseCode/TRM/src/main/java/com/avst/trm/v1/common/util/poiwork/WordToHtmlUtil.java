package com.avst.trm.v1.common.util.poiwork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import com.avst.trm.v1.common.util.log.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.apache.poi.xwpf.converter.core.FileImageExtractor;
import org.apache.poi.xwpf.converter.core.FileURIResolver;
import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
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


    public static void main(String[] args) {

        long starttime=(new Date()).getTime();
        String htmlpath="I:\\wubin\\笔录管理系统\\服务安装\\安装指导\\安装说明文档20190612（bl.exe）.html";
        String docpath="I:\\wubin\\笔录管理系统\\服务安装\\安装指导\\安装说明文档20190612（bl.exe）.docx";
        String imgpath = htmlpath.substring(0,htmlpath.lastIndexOf("\\")+1);
        System.out.println(imgpath);

        Word2007ToHtml(docpath,htmlpath);
        System.out.println((new Date()).getTime()-starttime);
    }

}
