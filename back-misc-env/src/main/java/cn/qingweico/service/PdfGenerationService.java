package cn.qingweico.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.qingweico.convert.Convert;
import cn.qingweico.model.Poem;
import cn.qingweico.network.NetworkUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @author zqw
 * @date 2025/8/9
 */
@Service
public class PdfGenerationService {


    public Map<String, Object> buildSend() {
        Poem poem = NetworkUtils.fetchDailyRecommendedPoem();
        return BeanUtil.beanToMap(poem);
    }
    public byte[] generatePdf(Map<String, Object> map) {
        Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();
            BaseFont bfChinese = BaseFont.createFont(
                    "STSong-Light",
                    "UniGB-UCS2-H",
                    BaseFont.NOT_EMBEDDED
            );

            Font titleFont = new Font(bfChinese, 18, Font.BOLD);
            Font authorFont = new Font(bfChinese, 12, Font.NORMAL);
            // 诗词题目设置
            Paragraph title = new Paragraph(Convert.toString(map.remove("title")), titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            // 诗词作者、朝代设置
            Paragraph author = new Paragraph(String.format("%s · %s", map.remove("author"),
                    map.remove("dynasty")), authorFont);
            author.setAlignment(Element.ALIGN_CENTER);
            document.add(author);

            // 其他内容设置
            Font contentFont = new Font(bfChinese, 12, Font.NORMAL);
            document.add(Chunk.NEWLINE);
            for (Object content : map.values()) {
                if (ObjectUtil.isNotEmpty(content)) {
                    Paragraph paragraph = new Paragraph(Convert.toString(content), contentFont);
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    document.add(paragraph);
                    document.add(Chunk.NEWLINE);
                }
            }
            document.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("生成PDF失败", e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
            try {
                out.close();
            } catch (IOException ignored) {
            }
        }
    }
}
