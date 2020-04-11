package wiki.sogou.jmail.netty.comm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @author JimYip
 */
public class PropertiesTools {

    public static String getStrProperty(String fileName, String propertyName) {
        Properties properties = new Properties();
        String returnString = "";
        try {
            InputStream is = new FileInputStream("D:\\temp\\" + fileName);
            BufferedReader bf = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            properties.load(bf);
            returnString = properties.getProperty(propertyName);
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        return returnString;
    }

}