package cn.qingweico.resource;

import cn.qingweico.resource.util.ResourceUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

/**
 * @author zqw
 * @date 2021/12/22
 */
public class EncodedFileSystemSourceLoader {
    public static void main(String[] args) throws IOException {
        String path = "src/main/java/cn/qingweico/resource/EncodedFileSystemSource.java";
        FileSystemResourceLoader loader = new FileSystemResourceLoader();
        Resource resource = loader.getResource(path);
        System.out.println(ResourceUtil.getResourceContext(resource));
    }
}
