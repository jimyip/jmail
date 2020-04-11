package wiki.sogou.jmail.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import wiki.sogou.jmail.netty.comm.InputStreamUtils;
import wiki.sogou.jmail.netty.comm.MyLogHander;
import wiki.sogou.jmail.netty.comm.PropertiesTools;

import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class SMTPServerHandler extends SimpleChannelInboundHandler<String> {
    private static final int CODE_HELLOTOCLIENT = 220;
    private static final int CODE_OK = 250;
    private static final int CODE_RECEIVEDATA = 354;
    private static final int CODE_BYETOCLIENT = 221;
    private static final int CODE_ERROR = 503;
    private String subject = "";
    private int count = 0;
    private List<String> recipients = new ArrayList<>();
    private String textCode = "";
    private String attachCode = "";
    private String textString = "";
    private String attachString = "";
    private String textCharsetCode;
    private String attaCharsetCode;
    private String attachFileName = "";
    private boolean isBody = false;
    private String boundary;
    private StringBuffer mailLogInfo = new StringBuffer();
    private StringBuffer serverDetailLogInfo = new StringBuffer();
    private String command;
    private final Class[] commandHandlerArgTypes = {String.class, StringTokenizer.class, ChannelHandlerContext.class};

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        new MyLogHander().initLogger();
        this.serverDetailLogInfo.append("\n----------开始----------");
        this.mailLogInfo.append("\n----------开始----------");
        this.serverDetailLogInfo.append("\n邮件客户端:").append(ctx.channel().remoteAddress()).append("连接上");
        send(220, "primetontest SMTP  (Postfix Rules!)", ctx);
        this.serverDetailLogInfo.append("\n发送字符串：220 primetontest SMTP  (Postfix Rules!)");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        send(503, "exceptionCaught!捕捉到异常!", ctx);
        this.serverDetailLogInfo.append("\n发送字符串：503 exceptionCaught!捕捉到异常!");

        this.serverDetailLogInfo.append("\n捕捉到异常，关闭连接:").append(cause.getCause()).append(":").append(cause.getMessage());
        this.mailLogInfo.append("\n捕捉到异常，关闭连接:").append(cause.getCause()).append(":").append(cause.getMessage()).append(Arrays.toString(cause.getStackTrace()));
        this.serverDetailLogInfo.append("\n----------结束----------");
        this.mailLogInfo.append("\n----------结束----------");
        MyLogHander.serverDetailLog.info(this.serverDetailLogInfo.toString());
        MyLogHander.mailInfoLog.info(this.mailLogInfo.toString());
        new MyLogHander().shutdownLogger();
        if (ctx.channel().isOpen()) {
            ctx.channel().close();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        this.serverDetailLogInfo.append("\n接收到字符串：").append(msg);
        msg = msg.trim();
        StringTokenizer st = new StringTokenizer(msg);
        String firstWord = "";
        if (st.hasMoreTokens()) {
            firstWord = st.nextToken().trim();
        }
        switch (firstWord) {
            case "EHLO":
                this.command = "ehlo";
                break;
            case "MAIL":
                this.command = "mail";
                break;
            case "RCPT":
                this.command = "rcpt";
                break;
            case "DATA":
                this.command = "data";
                break;
            case "Date:":
                this.command = "date";
                break;
            case "Subject:":
                this.command = "subject";
                break;
            case "QUIT":
                this.command = "quit";
                break;
            default:
                break;
        }

        Object[] args = {msg, st, ctx};
        Method commandHandler = getClass().getMethod("command_" + this.command, this.commandHandlerArgTypes);
        commandHandler.invoke(this, args);
    }

    public void command_ehlo(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        send(250, "OK", ctx);
        this.serverDetailLogInfo.append("\n发送字符串：250 OK");
    }

    public void command_mail(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        this.mailLogInfo.append("\n发件人：").append(msg.substring(msg.indexOf("<") + 1, msg.indexOf(">")));
        send(250, "OK", ctx);
        this.serverDetailLogInfo.append("\n发送字符串：250 OK");
    }

    public void command_rcpt(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        this.recipients.add(msg.substring(msg.indexOf("<") + 1, msg.indexOf(">")));
        send(250, "OK", ctx);
        this.serverDetailLogInfo.append("\n发送字符串：250 OK");
    }

    public void command_data(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        st = new StringTokenizer(msg);
        if ("DATA".equals(st.nextToken())) {
            for (String recipient : this.recipients) {
                this.mailLogInfo.append("\n收件人：").append(recipient);
            }
            send(354, "end with <CRLF>.<CRLF>", ctx);
            this.serverDetailLogInfo.append("\n发送字符串：354 end with <CRLF>.<CRLF>");
        }
    }

    public void command_date(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        st = new StringTokenizer(msg);
        if ("Date:".equals(st.nextToken())) {
            String receiveTime = msg.substring(msg.indexOf(",") + 1, msg.indexOf("+")).trim();
            SimpleDateFormat formatToDate = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.US);
            Date date = formatToDate.parse(receiveTime);
            SimpleDateFormat formatToTimeString = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
            receiveTime = formatToTimeString.format(date);
            this.mailLogInfo.append("\n邮件发送日期：").append(receiveTime);
        }
    }

    public void command_subject(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        st = new StringTokenizer(msg);
        String temp = st.nextToken();
        if ((!"Content-Type:".equals(temp)) && (!"mime-version:".equals(temp.toLowerCase()))
                && (temp.indexOf("boundary=\"") != 0)) {
            if ("Subject:".equals(temp)) {
                msg = MimeUtility.decodeText(msg).substring(8);
            } else {
                msg = MimeUtility.decodeText(msg);
            }
            this.subject += msg;
        } else if ("Content-Type:".equals(temp)) {
            this.mailLogInfo.append("\n邮件主题：").append(this.subject);
            st.nextToken();
            if (st.hasMoreTokens()) {
                this.boundary = st.nextToken().trim();
                this.boundary = this.boundary.substring(this.boundary.trim().indexOf("boundary=\"") + 10);
                this.boundary = this.boundary.substring(0, this.boundary.indexOf("\""));
                this.command = "boundary";
            }
        } else if (temp.indexOf("boundary=\"") == 0) {
            this.boundary = temp.trim().substring(temp.trim().indexOf("boundary=\"") + 10);
            this.boundary = this.boundary.substring(0, this.boundary.indexOf("\""));
            this.command = "boundary";
        }
    }

    /**
     * 获取正文信息
     *
     * @throws Exception
     */
    private void getTxtInfo() throws Exception {
        this.textString = InputStreamUtils.InputStreamTOString(
                MimeUtility.decode(new ByteArrayInputStream(this.textString.getBytes(this.textCharsetCode)),
                        this.textCode),
                this.textCharsetCode);
        this.mailLogInfo.append("\n邮件正文：").append(this.textString);
    }

    /**
     * 解析正文
     *
     * @throws Exception
     */
    private void pareTxtInfo(String msg) throws Exception {
        if (!isBodyMsg(msg)) {
            if (msg.contains("Content-Transfer-Encoding:")) {
                this.textCode = msg.substring(msg.indexOf("Content-Transfer-Encoding:") + 26).trim();
                this.serverDetailLogInfo.append("\n正文编码：" + this.textCode);
            }
            if (msg.contains("charset=")) {
                this.textCharsetCode = msg.substring(msg.indexOf("charset=") + 8);
            }
            return;
        }
        if (("quoted-printable".equals(this.textCode)) && (!"".equals(msg))
                && (msg.charAt(msg.length() - 1) == '=')) {
            msg = msg.substring(0, msg.length() - 1);
        }
        this.textString += msg;
    }

    /**
     * 获取附件信息
     *
     * @throws Exception
     */
    private void getAttachmentInfo() throws Exception {
        if ("".equals(this.attachFileName)) {
            this.attachString = InputStreamUtils.InputStreamTOString(MimeUtility.decode(
                    new ByteArrayInputStream(this.attachString.getBytes(this.attaCharsetCode)),
                    this.attachCode), this.attaCharsetCode);
            this.mailLogInfo.append("\n附正文内容：").append(this.attachString);
        } else {
            String attachPath = PropertiesTools.getStrProperty("common.properties",
                    "mail.fileHomeDir.Path");
            if (attachPath.charAt(attachPath.length() - 1) != '/') {
                attachPath = attachPath + "/";
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String timeTemp = dateFormat.format(new Date());
            this.mailLogInfo.append("\n附件存放地址：").append(attachPath).append(timeTemp).append(this.attachFileName);
            File file = new File(attachPath + timeTemp + this.attachFileName);
            InputStream is = new ByteArrayInputStream(this.attachString.getBytes());
            is = MimeUtility.decode(is, this.attachCode);
            InputStreamUtils.inputStreamToFile(is, file);
            this.mailLogInfo.append("\n文件接收成功!");
        }
    }

    /**
     * 解析附件
     *
     * @param msg
     * @throws Exception
     */
    private void pareAttachmentInfo(String msg, ChannelHandlerContext ctx) throws Exception {
        if (!isBodyMsg(msg)) {
            if (msg.contains("Content-Transfer-Encoding:")) {
                this.attachCode = msg.substring(msg.indexOf("Content-Transfer-Encoding:") + 26).trim();
                this.serverDetailLogInfo.append("\n附件/正文编码：" + this.attachCode);
            }
            if (msg.contains("charset=")) {
                this.attaCharsetCode = msg.substring(msg.indexOf("charset=") + 8);
            }

            if (msg.contains("filename=")) {
                this.attachFileName = msg.substring(msg.indexOf("filename=") + 9);
                if (this.attachFileName.contains("\"")) {
                    this.attachFileName = this.attachFileName.substring(1, this.attachFileName.length() - 1);
                }
                this.mailLogInfo.append("\n附件名：").append(this.attachFileName);
            }
            return;
        }
        if (("quoted-printable".equals(this.attachCode)) && (!"".equals(msg))
                && (msg.charAt(msg.length() - 1) == '=')) {
            msg = msg.substring(0, msg.length() - 1);
        }
        this.attachString += msg;
    }

    /**
     * 判断字符串是否为体数据
     *
     * @param msg
     * @return
     */
    private boolean isBodyMsg(String msg) {
        if (msg.isEmpty()) {
            this.isBody = true;
            return false;
        }
        return this.isBody;
    }

    /**
     * 判断是否读完
     *
     * @param msg
     * @return
     */
    private boolean isMailEnd(String msg) {
        return (this.count > 1) && (".".equals(msg));
    }

    /**
     * 初始化附件变量
     *
     * @throws Exception
     */
    private void initialAttachmentVariable() throws Exception {
        this.attachFileName = "";
        this.attachString = "";
        this.attachCode = "";
        this.attaCharsetCode = "";
    }

    /**
     * 解析正文和附件
     *
     * @param msg
     * @param st
     * @param ctx
     * @throws Exception
     */
    public void command_boundary(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        //判断是否读完
        if (isMailEnd(msg)) {
            send(250, "OK", ctx);
            this.serverDetailLogInfo.append("\n发送字符串：250 OK");
            return;
        }
        //读到边界
        if (msg.contains(this.boundary)) {
            isBody = false;
            //获取正文
            if (this.count == 1) {
                getTxtInfo();
                //获取附件或者附件正文,获取完后初始化附件变量
            } else if (this.count > 1) {
                getAttachmentInfo();
                initialAttachmentVariable();
            }
            this.count += 1;
        } else {
            //不是边界
            if (this.count == 1) {
                //解析正文
                pareTxtInfo(msg);
            } else {
                //解析附件
                pareAttachmentInfo(msg, ctx);
            }
        }
    }

    public void command_quit(String msg, StringTokenizer st, ChannelHandlerContext ctx) throws Exception {
        send(221, "GOOD BYE!", ctx);
        this.serverDetailLogInfo.append("\n发送字符串：250 OK");
        this.serverDetailLogInfo.append("\n----------结束----------");
        this.mailLogInfo.append("\n----------结束----------");
        MyLogHander.serverDetailLog.info(this.serverDetailLogInfo.toString());
        MyLogHander.mailInfoLog.info(this.mailLogInfo.toString());
        new MyLogHander().shutdownLogger();
        if (ctx.channel().isOpen()) {
            ctx.channel().close();
        }
    }

    private static void send(int code, String response, ChannelHandlerContext ctx) throws Exception {
        String line = code + " " + response + "\t\n";
        ctx.writeAndFlush(line);
    }
}