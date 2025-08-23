package cn.qingweico.afr.controller;

import cn.qingweico.afr.entity.User;
import cn.qingweico.afr.enums.FaceVerifyType;
import cn.qingweico.afr.model.GridFs;
import cn.qingweico.afr.servoce.impl.UserServiceImpl;
import cn.qingweico.afr.utils.CompareFace;
import cn.qingweico.io.FileUtils;
import cn.qingweico.model.ApiResponse;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.client.model.Filters;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author zqw
 * @date 2024/1/25
 */
@RestController
@RequestMapping("/fs")
public class GridController {
    @Resource
    private GridFSBucket gridFsBucket;

    @Value("${face.file.path}")
    private String faceFilePath;
    @Resource
    private CompareFace faceVerify;

    @Resource
    private UserServiceImpl userService;

    /**
     * 上传人脸信息至GridFs
     */
    @PostMapping("/uploadToGridFs")
    public ApiResponse<String> uploadToGridFs(@RequestBody GridFs gridFs) {
        // 获得图片的base64字符串
        String file64 = gridFs.getImg64();
        // 将base64字符串转换为字符数组
        byte[] bytes = Base64.getDecoder().decode(file64);
        // 转换为输入流
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        // 上传到gridFS中
        ObjectId id = gridFsBucket.uploadFromStream(gridFs.getUsername() + "." + "png", inputStream);
        // 获得文件在gridFS中的主键id
        String fileId = id.toString();
        return ApiResponse.ok(fileId);
    }

    /**
     * 查看人脸信息
     **/
    @GetMapping("/readFace64InGridFS/{faceId}")
    public ApiResponse<String> readFace64InGridFs(@PathVariable String faceId) throws IOException {
        if (StringUtils.isBlank(faceId)) {
            return ApiResponse.error("人脸信息不存在");
        }
        // 获得GridFs中人脸数据文件
        File face = readGridFsByFaceId(faceId);
        if (face == null) {
            return ApiResponse.error("人脸信息不存在");
        }
        // 转换人脸为base64
        String base64Face = FileUtils.fileToBase64(face);
        return ApiResponse.ok(base64Face);
    }

    private File readGridFsByFaceId(String faceId) throws IOException {
        GridFSFindIterable gridFsFiles = gridFsBucket.find(Filters.eq("_id", new ObjectId((faceId))));
        GridFSFile gridFs = gridFsFiles.first();
        if (gridFs == null) {
            return null;
        }
        String fileName = gridFs.getFilename();
        FileUtils.createDir(faceFilePath);
        // 获取文件流; 保存到本地或者服务器的临时目录
        File picFile = new File(faceFilePath + File.separator + fileName);
        // 创建文件输出流
        OutputStream os = Files.newOutputStream(picFile.toPath());
        // 下载到服务器或者本地
        gridFsBucket.downloadToStream(new ObjectId(faceId), os);
        return picFile;
    }

    @PostMapping("/login")
    public ApiResponse<String> face(@RequestBody GridFs loginBody) throws IOException {
        User user = userService.findOneByUsername(loginBody.getUsername());
        if (user == null) {
            return ApiResponse.error("用户不存在");
        }
        String base64 = loginBody.getImg64();
        if (StringUtils.isBlank(base64)) {
            return ApiResponse.error("用户人脸信息不存在");
        }
        // 从数据库中查询faceId
        String faceId = user.getFaceId();
        if (StringUtils.isBlank(faceId)) {
            return ApiResponse.error("用户人脸信息不存在");
        }
        // 请求文件服务, 获得人脸数据的base64数据
        ApiResponse<String> result = readFace64InGridFs(faceId);
        String base64Db = null;
        if (result != null) {
            base64Db = result.getData();
        }
        // 调用阿里AI进行人脸对比识别, 判断可行度, 从而实现人脸登陆
        boolean pass = faceVerify.faceVerify(FaceVerifyType.BASE64.getVal(), base64, base64Db, 60.0f);
        if (!pass) {
            return ApiResponse.error("人脸识别失败,请重试!");
        }
        return ApiResponse.success("人脸识别成功!");
    }
}
