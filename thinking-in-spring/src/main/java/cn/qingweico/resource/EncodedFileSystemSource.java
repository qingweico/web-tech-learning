package cn.qingweico.resource;

import cn.qingweico.resource.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.EncodedResource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * @author zqw
 * @date 2021/12/22
 */
public class EncodedFileSystemSource {
    public static void main(String[] args) throws IOException {
        File file = new File("src/main/java/cn/qingweico/resource/EncodedFileSystemSource.java");
        FileSystemResource fileSystemResource = new FileSystemResource(file);
        System.out.println(ResourceUtil.getResourceContext(fileSystemResource));
    }
}
