package wiki.sogou.jmail.netty.comm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MyLogHander extends Formatter {
    public static Logger serverDetailLog;
    public static Logger mailInfoLog;
    private static FileHandler serverDetailFileHandler;
    private static FileHandler mailInfoFileHandler;
    private static String logHomeDir;

    @Override
    public String format(LogRecord record) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        Date date = new Date();
        String str = df.format(date);
        return str + ":" + record.getLevel() + ":" + record.getMessage() + "\r\n";
    }

    private void initMailInfoLogger(String logDir) {
        mailInfoLog = Logger.getLogger("mylog2");
        mailInfoLog.setLevel(Level.INFO);
        try {
            mailInfoFileHandler = new FileHandler(logDir + "SMTPMailLog.log", true);
            mailInfoFileHandler.setLevel(Level.INFO);
            mailInfoFileHandler.setFormatter(new MyLogHander());
            mailInfoLog.addHandler(mailInfoFileHandler);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initgServerDetailLog(String logDir) {
        serverDetailLog = Logger.getLogger("mylog");
        serverDetailLog.setLevel(Level.INFO);
        try {
            serverDetailFileHandler = new FileHandler(logDir + "SMTPServerLog.log", true);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
        serverDetailFileHandler.setLevel(Level.INFO);
        serverDetailFileHandler.setFormatter(new MyLogHander());
        serverDetailLog.addHandler(serverDetailFileHandler);
    }

    public void initLogger() {
        logHomeDir = PropertiesTools.getStrProperty("common.properties",
                "mail.logHomeDir.Path");
        if (logHomeDir.charAt(logHomeDir.length() - 1) != '/') {
            logHomeDir += "/";
        }
        initMailInfoLogger(logHomeDir);
        initgServerDetailLog(logHomeDir);
    }

    public void shutdownLogger() {
        if (serverDetailLog != null) {
            serverDetailFileHandler.close();
            serverDetailLog.removeHandler(serverDetailFileHandler);
            serverDetailLog = null;
        }
        if (mailInfoLog != null) {
            mailInfoFileHandler.close();
            mailInfoLog.removeHandler(mailInfoFileHandler);
            mailInfoLog = null;
        }
    }
}