package com.avst.trm.v1.common.util.poiwork;

import com.avst.trm.v1.common.datasourse.police.entity.Police_answer;
import com.avst.trm.v1.common.datasourse.police.entity.moreentity.RecordToProblem;
import com.avst.trm.v1.common.util.LogUtil;
import com.avst.trm.v1.common.util.OpenUtil;
import com.avst.trm.v1.common.util.poiwork.param.Answer;
import com.avst.trm.v1.common.util.poiwork.param.Talk;
import com.avst.trm.v1.common.util.properties.PropertiesListenerConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
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

/**
 * 修改Word模板文件，并添加数据导出到一个新的Word文件
 */
public class XwpfTUtil {


    public static  void main(String[] args) {
        Map<String, String> params = new HashMap<>();

        params.put("${recordstarttime}", "152315455");
        params.put("${recordendtime}", "2019-6-25 11:57:12");
        params.put("${age}", "27");
        params.put("${username}", "吴斌");

        String path="C:\\Users\\Administrator\\Desktop\\ceshi.docx";
        String newfilepath="C:\\Users\\Administrator\\Desktop\\ceshi2.docx";

        List<Talk> talkList=new ArrayList<>();
        Talk talk=new Talk();
        List<Answer> list=new ArrayList<Answer>();
        Answer answer=new Answer();
        answer.setAnswer("答1");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问1");
        talkList.add(talk);
        talk=new Talk();
        list=new ArrayList<Answer>();
        answer=new Answer();
        answer.setAnswer("答2");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问2");
        talkList.add(talk);
        talk=new Talk();
        list=new ArrayList<Answer>();
        answer=new Answer();
        answer.setAnswer("答3");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问3");
        talkList.add(talk);
        talk=new Talk();
        list=new ArrayList<Answer>();
        answer=new Answer();
        answer.setAnswer("答4");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问411111111111111111111111111111111112222电饭锅电饭锅大股东电饭锅电饭锅十多个电饭锅电饭锅电饭锅电饭锅的");
        talkList.add(talk);
        talk=new Talk();
        list=new ArrayList<Answer>();
        answer=new Answer();
        answer.setAnswer("答5");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问5");
        talkList.add(talk);
        talk=new Talk();
        list=new ArrayList<Answer>();
        answer=new Answer();
        answer.setAnswer("答6");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问6");
        talkList.add(talk);
        talk=new Talk();
        list=new ArrayList<Answer>();
        answer=new Answer();
        answer.setAnswer("答7");
        list.add(answer);
        talk.setAnswers(list);
        talk.setQuestion("问7");
        talkList.add(talk);
        System.out.println((new Date()).getTime());
       replaceAndGenerateWord(path,newfilepath,params,talkList);
        System.out.println((new Date()).getTime());


//        List<String> slist=new ArrayList<String>();
//        slist.add("${age}");
//        slist.add("${username}");
//        File file=new File(path);
//        try {
//            InputStream inputStream=new FileInputStream(file);
//            checkWord(inputStream,"ceshi1.doc",slist);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

    }

    /**
     *套用模板文件生成对应的笔录文件
     * @param srcPath 模板文件路径
     * @param newfilepath 笔录文件路径
     * @param map 需要修改模板中的参数集合
     * @param talkList 插入最下方的问答集合，也可以是其他
     * @return
     */
    public static boolean replaceAndGenerateWord(String srcPath,String newfilepath, Map<String, String> map,  List<Talk> talkList){
        FileOutputStream outStream=null;
        XWPFDocument document=null;
        HWPFDocument document2=null;
        File tmpfile=null;//docx修改会把源文件修改掉，所以这里要加一个缓存文件
        try {

            String talkspace=PropertiesListenerConfig.getProperty("talkspace");
            if(StringUtils.isEmpty(talkspace)){
                talkspace="${talk}";
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

                map.put(talkspace,"\r\n");//docx不需要用替换的形式改talk，这里就直接给一个换行

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

                //组合谈话
                if(null!=talkList&&talkList.size() > 0){

                    XWPFParagraph xwpfParagraph=document.createParagraph();

                    boolean thelastone=true;//最后一个的时候一定不要回车
                    for(int i=talkList.size()-1;i>-1;i--){//必须反着来不然就会出现问答倒序排列
                        Talk talk=talkList.get(i);
                        if (null!=talk.getAnswers()&&talk.getAnswers().size()>0){
                            for (Answer answer : talk.getAnswers()) {
                                XWPFRun xwpfRun2=xwpfParagraph.insertNewRun(0);
                                xwpfRun2.setText(answer.getAnswer());//必须先写入答，在写入问，反着来
                                if(thelastone){
                                    thelastone=false;
                                }else{
                                    xwpfRun2.addCarriageReturn();//硬回车
                                }
                            }
                        }
                        XWPFRun xwpfRun=xwpfParagraph.insertNewRun(0);
                        xwpfRun.setText(talk.getQuestion());
                        xwpfRun.addCarriageReturn();//硬回车
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

                //组合谈话字符串
                if(null!=talkList&&talkList.size() > 0){
                    String talkstr="\r\n\r\n";
                    for(Talk talk:talkList){
                        talkstr+=talk.getQuestion()+"\r\n";
                        if (null!=talk.getAnswers()&&talk.getAnswers().size()>0){
                            for (Answer answer : talk.getAnswers()) {
                                talkstr+=answer.getAnswer()+"\r\n";
                            }
                        }
                    }
                    if(talkstr.endsWith("\r\n")){

                        talkstr=talkstr.substring(0,talkstr.lastIndexOf("\r\n"));
                    }
                    map.put(talkspace,talkstr);
                }

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

    /**
     * 检查Word模板文档是否正确
     * @param  inputStream
     * @param filename
     * @param list
     * @return 当返回的是一个不为空的list，才是正确的，null说明传入参数有误
     */
    public static List<String> checkWord(InputStream inputStream ,String filename,List<String> list){
        XWPFDocument document=null;
        HWPFDocument document2=null;
        try {

            if(null==list||list.size() ==0){
                return null;
            }

            String[] sp = filename.split("\\.");
            if ( sp.length <= 0) {
                return null;
            }
            if (!sp[sp.length - 1].equalsIgnoreCase("docx")
                    &&!(sp[sp.length - 1].equalsIgnoreCase("doc"))) {
                return null;
            }

            // 比较文件扩展名
            if (sp[sp.length - 1].equalsIgnoreCase("docx")) {

                document = new XWPFDocument(inputStream);
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
                        System.out.println(oneparaString+":oneparaString");
                        int i=0;
                        for(String mark:list){
                            if(oneparaString.equals(mark)){
                                list.remove(i);
                                break;
                            }
                            i++;
                        }
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
                            System.out.println(cellTextString+":cellTextString");
                            int j=0;
                            for(String mark:list){
                                if(cellTextString.equals(mark)){
                                    list.remove(j);
                                    break;
                                }
                                j++;
                            }
                        }
                    }
                }

            }else if ((sp[sp.length - 1].equalsIgnoreCase("doc"))) { // doc只能生成doc，如果生成docx会出错
                document2 = new HWPFDocument(inputStream);
                Range range = document2.getRange();
                int count=range.numParagraphs();
                for(int i=0;i<count;i++){
                    String text=range.getParagraph(i).text();
                    System.out.println(text+":text");
                    if(StringUtils.isEmpty(text)){
                        continue;
                    }
                    for(int j=0;j<list.size();j++){
                        String mark=list.get(j);
                        if(text.indexOf(mark) > -1){
                            list.remove(j);
                            j--;
                        }
                    }
                }
            }
            System.out.println("---list.size():"+list.size());

            return list;//当返回的是一个不为空的list，才是正确的，

        } catch (Exception e) {
            e.printStackTrace();
        }  finally {

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
        }
        return null;

    }

}
