package ohadcn.appify;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class AppifyWebClient extends WebViewClient {
    private String appUrl;
    private WebView mWebView;
    private Context mContext;

    public AppifyWebClient(Context c, WebView w, String url) {
        this(c, w, url, new AppifyWebInterface(c, w), new AppifyWebChromeClient());
    }

    public AppifyWebClient(WebView w, String url) {
        this(w.getContext(), w, url, new AppifyWebInterface(w.getContext(), w), new AppifyWebChromeClient());
    }

    public AppifyWebClient(Context c, WebView w, String url, AppifyWebInterface webInterface, WebChromeClient webChromeClient) {
        appUrl = url;
        mWebView = w;
        mContext = c;

        mWebView.addJavascriptInterface(webInterface, "appify");

        WebSettings webSettings = mWebView.getSettings();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        String databasePath = mContext.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        webSettings.setDatabasePath(databasePath);
        webSettings.setUserAgentString("Appify " + BuildConfig.VERSION_NAME + "/ Android " + Build.VERSION.CODENAME);

        if (0 != (c.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mWebView.setWebContentsDebuggingEnabled(true);
            }
        }

        mWebView.setWebChromeClient(webChromeClient);

    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(request.isForMainFrame()) {
                mWebView.loadUrl(appUrl);
                Toast.makeText(mContext, "Error occured, redirecting to main page", Toast.LENGTH_LONG).show();
            }
        }
        Log.d("WebResourceError", error.toString());
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        mWebView.setVisibility(View.VISIBLE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        if(request.getUrl().getScheme().startsWith("tel")) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(request.getUrl());
            mContext.startActivity(intent);
            return true;
        } else if(request.getUrl().getScheme().startsWith("app")) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            String activity = request.getUrl().getPath().replace("/", "");
            if(activity.startsWith(".")) {
                activity = request.getUrl().getHost() + activity;
            }
            intent.setComponent(new ComponentName(request.getUrl().getHost(), activity));
            mContext.startActivity(intent);
            return true;
        }

        return false;
    }
}
