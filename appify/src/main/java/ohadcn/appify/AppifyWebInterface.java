package ohadcn.appify;

import android.content.Context;
import android.webkit.WebView;

public class AppifyWebInterface extends RequestsWebInterface {

    public AppifyWebInterface(Context c, WebView w) {
        mContext = c;
        webView=w;
    }

}
