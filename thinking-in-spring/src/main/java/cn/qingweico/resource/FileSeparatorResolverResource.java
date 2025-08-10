package cn.qingweico.resource;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 关于 resolver.getResources 在不同OS下加载资源的问题
 * {@code String locationPattern = "classpath:XmlFileTpl/*.xml"}
 * 对比
 * {@code String locationPattern = "classpath:XmlFileTpl" + File.separator + "*.xml"}
 * 在windows中 路径分隔符是 反斜杠 \, 反斜杠 \ 在 Java 字符串中需要转义(\\), 并且 Linux 不识别
 * 在Linux中 路径分隔符是 正斜杠 /
 * 构建 System File Path 时可以使用File.separator
 * classpath 仅仅匹配到第一个位置
 * classpath* 匹配所有类路径位置, 包括所有 JAR 文件
 * 其中第二种方式在Windows下正常运行, 但是Linux下这行代码应该是报错了,因为下面的resourceMap是null
 * 这个错误至今未复现
 *
 * @author zqw
 * @date 2025/7/27
 */
@Slf4j
public class FileSeparatorResolverResource {

    private Map<String, String> resourceMap;

    public FileSeparatorResolverResource(String locationPattern) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource[] resources = resolver.getResources(locationPattern);
            resourceMap = new HashMap<>();
            init(resources);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public static void main(String[] args) {
        String locationPattern = "classpath:XmlFileTpl/*.xml";
        FileSeparatorResolverResource osResolverResource = new FileSeparatorResolverResource(locationPattern);
        System.out.println(osResolverResource.resourceMap);
    }

    public void init(Resource[] resources) throws IOException {
        for (Resource resource : resources) {
            processResource(resource);
        }
    }

    private void processResource(Resource resource) throws IOException {
        File file = resource.getFile();
        if (file.isDirectory()) {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    processResource(new FileSystemResource(child));
                }
            }
        } else {
            String content = FileCopyUtils.copyToString(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
            resourceMap.put(file.getName(), content);
        }
    }
}
