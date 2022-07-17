package cn.qingweico.resource;

import cn.qingweico.resource.util.ResourceUtil;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author zqw
 * @date 2021/12/22
 */
public class CustomizedResourcePatternResolver {
    public static void main(String[] args) throws IOException {
        String path = "src/main/java/cn/qingweico/resource/";
        String locationPattern = path + "*.java";
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(new FileSystemResourceLoader());
        resolver.setPathMatcher(new JavaFilePathMatcher());
        Resource[] resources = resolver.getResources(locationPattern);
        Stream.of(resources).map(ResourceUtil::getResourceContext).forEach(System.out::println);
    }

    static class JavaFilePathMatcher implements PathMatcher {
        @Override
        public boolean isPattern(String path) {
            return path.endsWith(".java");
        }

        @Override
        public boolean match(String pattern, String path) {
            return path.endsWith(".java");
        }

        @Override
        public boolean matchStart(String pattern, String path) {
            return false;
        }

        @Override
        public String extractPathWithinPattern(String pattern, String path) {
            return null;
        }

        @Override
        public Map<String, String> extractUriTemplateVariables(String pattern, String path) {
            return null;
        }

        @Override
        public Comparator<String> getPatternComparator(String path) {
            return null;
        }

        @Override
        public String combine(String pattern1, String pattern2) {
            return null;
        }
    }
}
