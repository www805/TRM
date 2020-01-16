package com.avst.trm.v1.common.util.poiwork;




import com.aspose.words.Document;
import com.aspose.words.FontSettings;
import com.aspose.words.ReadLicense;
import com.aspose.words.SaveFormat;
import com.avst.trm.v1.common.util.log.LogUtil;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import com.avst.trm.v1.common.util.sq.NetTool;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Date;

/**
 * Word转PDF
 */
public class WordToPDF {



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

    /**
     *
     * @param pdfpath 输出PDF的路径
     * @param wordpath 待转换word的路径
     */
    public static boolean word2pdf(String pdfpath,String wordpath) {

        if(StringUtils.isEmpty(pdfpath)||StringUtils.isEmpty(wordpath)){
            return false;
        }
        File file=new File(wordpath);
        if(!file.exists()){
            return false;
        }
        File file2=new File(pdfpath);
        try {
            if(!file2.exists()){
                file2.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        boolean bool=false;
        long starttime=(new Date()).getTime();
        FileOutputStream os=null;
        try {
            if(!getLicense()){// 验证License 若不验证则转化出的PDP文档会有水印产生
                LogUtil.intoLog(4,WordToPDF.class,"word2pdf 验证License 失败");
            }

            os = new FileOutputStream(file2);
            LogUtil.intoLog(1,WordToPDF.class,"wordpath------------------------------"+wordpath);
            Document doc = new Document(wordpath);                    //Address是将要被转化的word文档

            if(NetTool.osType()==2){//这里是对Linux系统的中文字体库的处理
                String chineseFontsPath= PropertiesListenerConfig.getProperty("chineseFontsPath") ;
                if(StringUtils.isNotEmpty(chineseFontsPath)){
                    File file1=new File(chineseFontsPath);
                    if(null!=file1&&file1.isDirectory()){
                        FontSettings.setFontsFolder(chineseFontsPath, true);
                    }
                }
            }

            doc.save(os, SaveFormat.PDF);                            //全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            os.close();
            bool=true;
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(null!=os){
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endtime=(new Date()).getTime();
        LogUtil.intoLog(1,WordToPDF.class,wordpath+":wordpath----word2pdf time:"+(endtime-starttime));
        return bool;
    }


    public static void main(String[] args) {

        long starttime=(new Date()).getTime();
        String PDFpath="C:\\Users\\admin\\Desktop\\测试情绪报告001.pdf";
        String docpath="D:\\trmfile\\upload\\zips\\recordwordOrpdf\\2019\\11\\27\\刘一《anjian》提讯会议_行政询问笔录_第19次.doc";
        System.out.println(word2pdf(PDFpath,docpath)+"----wordTopdf(docpath,PDFpath)");

        System.out.println((new Date()).getTime()-starttime);
    }

}
