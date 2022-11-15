package cn.qingweico;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author zqw
 * @date 2022/08/14
 */
@Path("/hello")
public class ExampleResource {

    // gu install native-image 安装native支持
    // 打包项目 mvn package -Pnative -DskipTests

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from RESTEasy Reactive";
    }
}