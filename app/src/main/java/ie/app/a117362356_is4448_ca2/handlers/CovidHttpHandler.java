package ie.app.a117362356_is4448_ca2.handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CovidHttpHandler {

    public static String getStats(String uri) {

        String s = "no response";
        HttpURLConnection conn = null;
        int http_status = 0;

        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection)url.openConnection();
            InputStream in = conn.getInputStream();
            http_status = conn.getResponseCode();

            if (http_status == 200) {
                s = streamToString(in); //This is the response
            } else {
                s = "bad response";
            }
        } catch (MalformedURLException m) {
            s = "malformed URL exception";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
        }
        return s;
    }

    private static String streamToString(InputStream in) throws IOException {
        StringBuilder out = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        for(String line = br.readLine(); line != null; line = br.readLine())
            out.append(line);
        br.close();
        return out.toString();
    }
}
