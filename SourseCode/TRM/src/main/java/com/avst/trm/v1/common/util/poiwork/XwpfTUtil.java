package com.avst.trm.v1.common.util.poiwork;

import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.poiwork.param.Talk;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.StringUtil;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlToken;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XwpfTUtil {


    public static  void main(String[] args) {
        Map<String, String> params = new HashMap<>();

        params.put("${time}", "152315455");
        params.put("${date}", "2019-6-25 11:57:12");
        params.put("${age}", "");
        params.put("${username}", "吴斌");

        String path="C:\\Users\\Administrator\\Desktop\\ceshi1.docx";
//        String path="i:\\wubin\\ceshi2.docx";
        String newfilepath="C:\\Users\\Administrator\\Desktop\\ceshi2.docx";
//        XwpfTUtil xwpfTUtil=new XwpfTUtil();
//        xwpfTUtil.replaceInPara(path,newpath,params);

        List<Talk> talkList=new ArrayList<>();
        Talk talk=new Talk();
        talk.setAnswer("答1");
        talk.setQuestion("问1");
        talkList.add(talk);
        talk=new Talk();
        talk.setAnswer("答2");
        talk.setQuestion("问2");
        talkList.add(talk);
        talk=new Talk();
        talk.setAnswer("答3");
        talk.setQuestion("问3");
        talkList.add(talk);
        talk=new Talk();
        talk.setAnswer("答4");
        talk.setQuestion("问4");
        talkList.add(talk);
        talk=new Talk();
        talk.setAnswer("答5");
        talk.setQuestion("问5");
        talkList.add(talk);
        talk=new Talk();
        talk.setAnswer("答6");
        talk.setQuestion("问6");
        talkList.add(talk);
        talk=new Talk();
        talk.setAnswer("答7");
        talk.setQuestion("问7");
        talkList.add(talk);
        System.out.println((new Date()).getTime());
        replaceAndGenerateWord(path,newfilepath,params,talkList);
        System.out.println((new Date()).getTime());
    }

    /**
     *套用模板文件生成对应的笔录文件
     * @param srcPath 模板文件路径
     * @param newfilepath 笔录文件路径
     * @param map 需要修改模板中的参数集合
     * @param talkList 插入最下方的问答集合，也可以是其他
     * @return
     */
    public static boolean replaceAndGenerateWord(String srcPath,String newfilepath, Map<String, String> map, List<Talk> talkList){
        FileOutputStream outStream=null;
        XWPFDocument document=null;
        HWPFDocument document2=null;
        File tmpfile=null;//docx修改会把源文件修改掉，所以这里要加一个缓存文件
        try {

            String talkspace=PropertiesListenerConfig.getProperty("talkspace");
            if(StringUtils.isEmpty(talkspace)){
                talkspace="${talk}";
            }

            //组合谈话字符串
            if(null!=talkList&&talkList.size() > 0){
                String talkstr="\r\n\r\n";
                for(Talk talk:talkList){
                    talkstr+=talk.getQuestion()+"\r\n"+talk.getAnswer()+"\r\n";
                }
                map.put(talkspace,talkstr);
            }

            String[] sp = srcPath.split("\\.");
            if ( sp.length <= 0) {
                return false;
            }
            String tmpPath= OpenUtil.getfile_folder(newfilepath)+"\\tmp."+sp[1];
            String[] dp = newfilepath.split("\\.");
            // 判断文件有无扩展名
            if ( dp.length <= 0) {
                return false;
            }
            File newfile=new File(newfilepath);
            tmpfile=new File(tmpPath);
            File oldfile=new File(srcPath);
            if(!newfile.exists()){
                newfile.createNewFile();
            }


            if (!sp[sp.length - 1].equalsIgnoreCase("docx")
                    &&!(sp[sp.length - 1].equalsIgnoreCase("doc")
                    && dp[dp.length - 1].equalsIgnoreCase("doc")) ) {
                return false;
            }

            // 比较文件扩展名
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {

                if(!tmpfile.exists()){
                    tmpfile.createNewFile();
                }
                FileUtils.copyFile(oldfile,tmpfile);

                document = new XWPFDocument(POIXMLDocument.openPackage(tmpPath));
                // 替换段落中的指定文字
                Iterator<XWPFParagraph> itPara = document.getParagraphsIterator();
                while (itPara.hasNext()) {
                    XWPFParagraph paragraph = itPara.next();
                    List<XWPFRun> runs = paragraph.getRuns();
                    for (XWPFRun run : runs) {
                        String oneparaString = run.getText(run.getTextPosition());
                        if (StringUtils.isBlank(oneparaString)){
                            continue;
                        }
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            oneparaString = oneparaString.replace(entry.getKey(), entry.getValue());
                        }
                        run.setText(oneparaString, 0);
                    }
                }

                // 替换表格中的指定文字
                Iterator<XWPFTable> itTable = document.getTablesIterator();
                while (itTable.hasNext()) {
                    XWPFTable table = itTable.next();
                    int rcount = table.getNumberOfRows();
                    for (int i = 0; i < rcount; i++) {
                        XWPFTableRow row = table.getRow(i);
                        List<XWPFTableCell> cells = row.getTableCells();
                        for (XWPFTableCell cell : cells) {
                            String cellTextString = cell.getText();
                            for (Map.Entry<String, String> e : map.entrySet()) {
                                cellTextString = cellTextString.replace(e.getKey(), e.getValue());
                            }
                            cell.removeParagraph(0);
                            cell.setText(cellTextString);
                        }
                    }
                }

                outStream = new FileOutputStream(newfile);
                document.write(outStream);

                return true;
            }else if ((sp[sp.length - 1].equalsIgnoreCase("doc"))
                    && (dp[dp.length - 1].equalsIgnoreCase("doc"))) { // doc只能生成doc，如果生成docx会出错
                document2 = new HWPFDocument(new FileInputStream(oldfile));
                Range range = document2.getRange();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    range.replaceText(entry.getKey(), entry.getValue());
                }

                outStream = new FileOutputStream(newfile);
                document2.write(outStream);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally {

            if (outStream != null) {
                try {
                    outStream.flush();
                    outStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (document != null) {
                try {
                    document.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (document2 != null) {
                try {
                    document2.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (tmpfile != null) {
                try {
                    tmpfile.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return false;

    }


    //word 转PDF
    //检测Word模板是否正确



}
