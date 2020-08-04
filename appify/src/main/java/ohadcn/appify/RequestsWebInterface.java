package ohadcn.appify;

import android.webkit.JavascriptInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RequestsWebInterface extends NotificationsWebInterface {

    @JavascriptInterface
    public void get(final String url, final String callback) {
        get(url, callback, new String[0]);
    }

    /**
     * @hide
     * @param url
     * @param callback
     * @param headers
     */
    @JavascriptInterface
    public void get(final String url, final String callback, final String[] headers) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                loadJavascript(callback + "(\"success\": true, \"response\": \""+response+"\");");
            } else {
                loadJavascript(callback + "(\"success\": false, \"errorType\": \"http\", \"error\": "+responseCode+");");
            }
        } catch (java.io.IOException e) {
            loadJavascript(callback + "(\"success\": false, \"errorType\": \"io\", \"error\": "+e.getMessage()+");");
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void post(final String url, final String callback, String data) {
        post(url, data, callback, new String[0]);
    }

    /**
     * @hide
     * @param url
     * @param callback
     * @param data
     * @param headers
     */
    @JavascriptInterface
    public void post(final String url, final String callback, final String data, final String[] headers) {
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.getOutputStream().write(data.getBytes());
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                loadJavascript(callback + "(\"success\": true, \"response\": \""+response+"\");");
            } else {
                loadJavascript(callback + "(\"success\": false, \"errorType\": \"http\", \"error\": "+responseCode+");");
            }
        } catch (java.io.IOException e) {
            loadJavascript(callback + "(\"success\": false, \"errorType\": \"io\", \"error\": "+e.getMessage()+");");
            e.printStackTrace();
        }
    }

}
