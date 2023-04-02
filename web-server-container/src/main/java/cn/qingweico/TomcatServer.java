package cn.qingweico;

import org.apache.catalina.*;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;

/**
 * @author zqw
 * @date 2023/3/11
 */
public class TomcatServer {
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();

        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(8080);

        Engine engine = new StandardEngine();
        engine.setDefaultHost("localhost");

        Host host = new StandardHost();
        host.setName("localhost");

        String contextPath = "";

        Context context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);

        service.setContainer(engine);
        service.addConnector(connector);


        tomcat.start();

    }
}
