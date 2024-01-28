package cn.qingweico.alifacerecognition.controller;

import cn.qingweico.alifacerecognition.model.GridFs;
import cn.qingweico.alifacerecognition.utils.FileUtils;
import cn.qinwweico.model.ApiResponse;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

/**
 * @author zqw
 * @date 2024/1/25
 */
public class UploadController {
    @Resource
    private GridFSBucket gridFsBucket;

    private static final String FACE_PIC_PATH;

    static {
        String os = System.getProperty("os.name");
        if ("Windows 10".equals(os)) {
            FACE_PIC_PATH = "C://facePic";
        } else {
            FACE_PIC_PATH = "/home/java/facePic";
        }
    }
    @PostMapping("/uploadToGridFs")
    public ApiResponse uploadToGridFs(@RequestBody GridFs gridFs) throws IOException {

        // 获得图片的base64字符串
        String file64 = gridFs.getImg64();
        // 将base64字符串转换为字符数组
        byte[] bytes = new BASE64Decoder().decodeBuffer(file64);
        // 转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        // 上传到gridFS中
        ObjectId id = gridFsBucket.uploadFromStream(gridFs.getUsername() + "." + "png", inputStream);
        // 获得文件在gridFS中的主键id
        String fileId = id.toString();
        return ApiResponse.ok(fileId);
    }

    @GetMapping("/readInGridFS")
    public void readInGridFs(String faceId, HttpServletResponse resp) throws IOException {

        if (StringUtils.isBlank(faceId)) {
            return;
        }

        // 从gridFS中读取文件
        File adminFace = readGridFsByFaceId(faceId);

        // 把人脸图片输出到浏览器
        FileUtils.downloadFileByStream(resp, adminFace);
    }

    @GetMapping("/readFace64InGridFS")
    public ApiResponse readFace64InGridFs(String faceId) throws IOException {

        // 获得GridFs中人脸数据文件
        File face = readGridFsByFaceId(faceId);

        // 转换人脸为base64
        String base64Face = FileUtils.fileToBase64(face);
        return ApiResponse.ok(base64Face);
    }
    private File readGridFsByFaceId(String faceId) throws IOException {

        GridFSFindIterable gridFsFiles = gridFsBucket.find(Filters.eq("_id", new ObjectId((faceId))));
        GridFSFile gridFs = gridFsFiles.first();
        if (gridFs == null) {
            throw new FileNotFoundException("人脸信息不存在");
        }
        String fileName = gridFs.getFilename();
        // 获取文件流; 保存到本地或者服务器的临时目录
        File picFile = new File(FACE_PIC_PATH + "/" + fileName);
        // 创建文件输出流
        OutputStream os = Files.newOutputStream(picFile.toPath());
        // 下载到服务器或者本地
        gridFsBucket.downloadToStream(new ObjectId(faceId), os);
        return picFile;
    }

    @GetMapping("/removeGridFsFile")
    public ApiResponse removeGridFsFile(String faceId) {
        gridFsBucket.delete(new ObjectId(faceId));
        return ApiResponse.ok();
    }
}
