package br.com.mcm.apimcmfood;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.springframework.util.StreamUtils;
public class AdicionaCorpoJson {
    public static String getContentFromResource(String resourceName) {
        try {
            InputStream stream = AdicionaCorpoJson.class.getResourceAsStream(resourceName);
            return StreamUtils.copyToString(stream, Charset.forName("UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
