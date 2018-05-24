package cn.gloomy.h;

import java.io.FileInputStream;
import java.util.Properties;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author yunfu.wang
 * @version $ EmbbedJetty 2016-1-13 下午7:13:10 $
 */
public class JettyLauncher {

  private static Logger logger          = LoggerFactory.getLogger(JettyLauncher.class);

  private String        bindHost        = "0.0.0.0";                                   // 默认绑定主机IP

  private int           bindPort        = 80;                                          // 默认绑定主机端口

  private int           minThreads      = 15;                                          // 默认线程池的最小数量

  private int           maxThreads      = 30;                                          // 默认线程池的最大数量

  private int           acceptors       = 2;                                           // 默认监听请求线程数

  private int           acceptQueueSize = 100;                                         // 默认允许等待的请求队列大小

  private int           maxIdleTime     = 30000;                                       // 默认线程最大空闲时间为30秒

  private String        contextPath     = "/";                                         // 启动的工程路径名

  public void start() throws Exception {

    Properties pro = new Properties();
    FileInputStream in = new FileInputStream(this.getClass().getResource("/").getPath() + "config.properties");
    pro.load(in);
    in.close();

     if (pro.getProperty("biz.bind.host") != null) {
     bindHost = pro.getProperty("biz.bind.host");
     }
    if (pro.getProperty("biz.bind.port") != null) {
      bindPort = Integer.parseInt(pro.getProperty("biz.bind.port"));
    }
    // if (pro.getProperty("biz.threads.min.num") != null) {
    // minThreads = Integer.parseInt(pro.getProperty("biz.threads.min.num"));
    // }
    // if (pro.getProperty("biz.threads.max.num") != null) {
    // maxThreads = Integer.parseInt(pro.getProperty("biz.threads.max.num"));
    // }
    // if (pro.getProperty("biz.threads.acceptors.num") != null) {
    // acceptors =
    // Integer.parseInt(pro.getProperty("biz.threads.acceptors.num"));
    // }
    // if (pro.getProperty("biz.threads.accept.queue.size") != null) {
    // acceptQueueSize =
    // Integer.parseInt(pro.getProperty("biz.threads.accept.queue.size"));
    // }
    // if (pro.getProperty("biz.threads.max.idle.time") != null) {
    // maxIdleTime =
    // Integer.parseInt(pro.getProperty("biz.threads.max.idle.time"));
    // }

    Server server = new Server();

    QueuedThreadPool threadPool = new QueuedThreadPool();
    threadPool.setMinThreads(minThreads);
    threadPool.setMaxThreads(maxThreads);

    ContextHandlerCollection handler = new ContextHandlerCollection();

    SelectChannelConnector connector = new SelectChannelConnector();
    connector.setHost(bindHost);
    connector.setPort(bindPort);
    // 每个请求被accept前允许等待的连接数
    connector.setAcceptQueueSize(acceptQueueSize);
    // 同事监听read事件的线程数
    connector.setAcceptors(acceptors);
    // 连接最大空闲时间，默认是200000，-1表示一直连接
    connector.setMaxIdleTime(maxIdleTime);
    connector.setThreadPool(threadPool);
    connector.setUseDirectBuffers(false);

    server.addConnector(connector);

    WebAppContext webapp = new WebAppContext();
    webapp.setContextPath(contextPath);
    webapp.setResourceBase("./webapp");
    // String currentDir = new File(".").getParent();
    // File workDir = new File(currentDir, "jsp");
    // webapp.setTempDirectory(workDir);

    handler.addHandler(webapp);

    server.setHandler(handler);

    server.start();

  }

  public int getBindPort() {
    return bindPort;
  }

  public void setBindPort(int bindPort) {
    this.bindPort = bindPort;
  }

  public String getBindHost() {
    return bindHost;
  }

  public void setContextPath(String contextPath) {
    this.contextPath = contextPath;
  }

  public static void main(String[] args) {

    String contextPath = null;
    for (String arg : args) {
      if (arg.startsWith("-contextPath")) {
        contextPath = arg.substring(arg.indexOf("=") + 1);
      }
    }

    JettyLauncher embbedJetty = new JettyLauncher();
    if (contextPath != null && contextPath.length() > 0) {
      embbedJetty.setContextPath(contextPath);
    }

    try {

      embbedJetty.start();

      logger.warn("Server start in [" + embbedJetty.getBindHost() + ":" + embbedJetty.getBindPort() + "]");

    } catch (Exception e) {
      logger.error("Server Start Error: ", e);
      System.exit(-1);
    }
  }
}
