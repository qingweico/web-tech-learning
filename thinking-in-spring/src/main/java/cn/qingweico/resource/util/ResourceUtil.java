package cn.qingweico.resource.util;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;

import java.io.IOException;
import java.io.Reader;

/**
 * @author zqw
 * @date 2021/12/22
 */
public interface ResourceUtil {
     static String getResourceContext(Resource resource) {
        EncodedResource encodedResource = new EncodedResource(resource, "UTF-8");
        try (Reader reader = encodedResource.getReader()) {
            return IOUtils.toString(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
