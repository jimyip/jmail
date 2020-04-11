package wiki.sogou.jmail.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import wiki.sogou.jmail.netty.comm.MyLogHander;

/**
 * @author JimYip
 */
public class SMTPServer {
    public static boolean SEVER_STATUS = false;
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;

    public static void run() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new SMTPServerInitializer());

            System.out.println("---Server start---");
            SEVER_STATUS = true;

            ChannelFuture f = b.bind(25).sync();

            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            System.out.println("---Server shutdown---");
        }
    }

    public static void main(String[] args) {
        startupServer();
    }

    public static void startupServer() {
        try {
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void shutdownServer() {
        if ((bossGroup != null) && (!bossGroup.isShutdown())) {
            bossGroup.shutdownGracefully();
            new MyLogHander().shutdownLogger();
        }
        if ((workerGroup != null) && (!workerGroup.isShutdown())) {
            workerGroup.shutdownGracefully();
            new MyLogHander().shutdownLogger();
        }
    }
}