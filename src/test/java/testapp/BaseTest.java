package testapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {
    private final Properties prop = new Properties();

    public BaseTest() {
        try {
            prop.load(new FileInputStream("test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String createURL(String ...counterTypes) {
        StringBuilder types = new StringBuilder();
        for (String type: counterTypes) {
            types.append(type).append(",");
        }
        types.deleteCharAt(types.length() - 1);
        return String.format("https://api.ok.ru/fb.do?application_key=%s&counterTypes=%s&format=%s&group_id=%s&method=%s&sig=%s&access_token=%s",
                prop.getProperty("application_key"),
                types,
                prop.getProperty("format"),
                prop.getProperty("group_id"),
                prop.getProperty("method"),
                prop.getProperty("sig"),
                prop.getProperty("access_token"));
    }




}
