package ohadcn.appify;

import android.content.Context;
import android.webkit.WebView;

abstract class BaseWebInterface {
    protected Context mContext;
    protected WebView webView;

    public void loadJavascript(final String func) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + func);
            }
        });
    }

}
