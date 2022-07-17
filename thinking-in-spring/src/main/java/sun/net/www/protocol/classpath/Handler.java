package sun.net.www.protocol.classpath;

import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

/**
 * classpath 协议实现 {@link URLStreamHandler}
 *
 * @author zqw
 * @date 2021/12/22
 */
public class Handler extends URLStreamHandler {
    @Override
    protected URLConnection openConnection(URL u) {
        return new ClasspathHandler(u);
    }
}
