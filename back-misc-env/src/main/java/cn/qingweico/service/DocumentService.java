package cn.qingweico.service;

import cn.qingweico.convert.StringConvert;
import cn.qingweico.datetime.DateUtil;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/22
 */
@Service
public class DocumentService {
    public byte[] generateDocument(Map<String, Object> map) {
        try {
            XWPFDocument document = new XWPFDocument();

            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText(StringConvert.toString(map.remove("title")));
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            XWPFRun authorRun = title.createRun();
            authorRun.addBreak();
            authorRun.setText(String.format("%s · %s",
                    StringConvert.toString(map.remove("author")),
                    StringConvert.toString(map.remove("dynasty"))));
            authorRun.setFontSize(12);
            authorRun.setItalic(true);

            for (String key : map.keySet()) {
                XWPFParagraph paragraph = document.createParagraph();
                XWPFRun run = paragraph.createRun();
                run.setText(StringConvert.toString(map.get(key)));
                run.setFontSize(12);
            }

            XWPFParagraph dateParagraph = document.createParagraph();
            XWPFRun dateRun = dateParagraph.createRun();
            dateRun.setText(DateUtil.now());
            dateRun.setItalic(true);
            dateRun.setFontSize(10);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.write(out);
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成Word失败: " + e.getMessage());
        }
    }
}
