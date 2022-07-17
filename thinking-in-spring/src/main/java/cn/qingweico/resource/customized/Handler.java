package cn.qingweico.resource.customized;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 自定义实现 Handler
 *
 * @author zqw
 * @date 2021/12/22
 */
public class Handler extends sun.net.www.protocol.classpath.Handler {
   // VM Options: -Djava.protocol.handler.pkgs=cn.qingweico.resource
   public static void main(String[] args) throws IOException {
      URL url = new URL("customized:///META-INF/user.properties");
      InputStream inputStream = url.openStream();
      System.out.println(StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8));
   }
}
