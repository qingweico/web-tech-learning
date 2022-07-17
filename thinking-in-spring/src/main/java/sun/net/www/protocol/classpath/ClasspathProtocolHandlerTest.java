package sun.net.www.protocol.classpath;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author zqw
 * @date 2021/12/22
 */
public class ClasspathProtocolHandlerTest {
   public static void main(String[] args) throws IOException {
      URL url = new URL("classpath:///META-INF/user.properties");
      InputStream inputStream = url.openStream();
      System.out.println(StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8));

   }
}
