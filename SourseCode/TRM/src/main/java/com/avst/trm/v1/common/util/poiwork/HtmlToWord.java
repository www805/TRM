package com.avst.trm.v1.common.util.poiwork;

import com.avst.trm.v1.common.util.log.LogUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

/**
 * html标签转word
 */
public class HtmlToWord {

    /**
     *
     * @param wordpath 导出后word地址
     * @param htmlcontent 需要转word的html内容
     * @return
     */
    public static boolean HtmlToWord(String wordpath, String htmlcontent){
        if (StringUtils.isNotEmpty(wordpath)){
            htmlcontent = "<html><meta charset='UTF-8'><head></head><body>"+htmlcontent+"</body></html>";
            byte b[] = htmlcontent.getBytes();
            try {
                InputStream is = new ByteArrayInputStream(b);
                OutputStream os = new FileOutputStream(wordpath);
                POIFSFileSystem fs = new POIFSFileSystem();
                fs.createDocument(is, "WordDocument");
                fs.writeFilesystem(os);
                os.close();
                is.close();
                LogUtil.intoLog(4,WordToHtmlUtil.class,"HtmlToWord HTML转WORD成功"+wordpath);
                return  true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        LogUtil.intoLog(4,WordToHtmlUtil.class,"HtmlToWord HTML转WORD失败"+wordpath);
        return false;
    }

    public static void main(String[] args) {
        String content = "<div><p style='color: #999'>【赵六】 2019-11-02 13:31:12</p><span style='color: #fff; background: #0181cc;'>但是。</span></div>" +
                " <div><p style='color: #999'>2019-11-02 13:31:12 【赵六】 </p> <span  style='color: #fff; background: #ef8201;float:right'>八嘎。</span>  </div>";
        content = "<html><meta charset='UTF-8'><head>情绪报告</head><body>"+content+"</body></html>";
        byte b[] = content.getBytes();
        try {
            InputStream is = new ByteArrayInputStream(b);
            OutputStream os = new FileOutputStream("f:\\1.doc");
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
