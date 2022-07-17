package sun.net.www.protocol.classpath;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * ClasspathHandler {@link URLConnection} 实现
 *
 * @author zqw
 * @date 2021/12/22
 */
public class ClasspathHandler extends URLConnection {
    private final ClassPathResource resource;

    /**
     * Constructs a URL connection to the specified URL. A connection to
     * the object referenced by the URL is not created.
     *
     * @param url the specified URL.
     */
    protected ClasspathHandler(URL url) {
        super(url);
        this.resource = new ClassPathResource(url.getPath());
    }

    @Override
    public void connect() throws IOException {


    }

    @Override
    public InputStream getInputStream() throws IOException {
        return resource.getInputStream();
    }
}
