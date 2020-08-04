package com.appify;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.URL;

import ohadcn.appify.AppifyWebChromeClient;
import ohadcn.appify.AppifyWebClient;
import ohadcn.appify.AppifyWebInterface;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 3;
    private static final int REQUEST_SELECT_FILE = 4;

    private WebView mWebView;

    private ValueCallback<Uri[]> filePathCallback = null;
    private ValueCallback<Uri> fileChooserCallback = null;
    public String appUrl = "https://www.example.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mWebView = findViewById(R.id.webView);

        appUrl = getString(R.string.app_url);
        mWebView.setWebViewClient(new AppifyWebClient(this, mWebView, appUrl, new AppifyWebInterface(this, mWebView), new AppifyWebChromeClient()));
        mWebView.loadUrl(appUrl);
        Intent intent;
        if ((intent = getIntent()) != null)
            handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack())
            mWebView.goBack();
        else {
            mWebView.loadUrl(appUrl);
        }
    }

    private void handleIntent(final Intent caller) {
        Uri data;
        System.out.println(caller);
        if (caller == null)
            return;
        String scheme;
        if ((data = caller.getData()) != null && (scheme = data.getScheme()) != null) {
            if (scheme.equals("http") || scheme.equals("https")) {
                if (data.getHost()
                        .equals(getString(R.string.app_domain)) || BuildConfig.DEBUG) {
                    mWebView.loadUrl(data.toString());
                }
            } else if (scheme.equals(getString(R.string.app_scheme))) {
                if (data.getHost()
                        .equals(getString(R.string.app_domain)) || BuildConfig.DEBUG) {
                    mWebView.loadUrl(data.toString().replaceFirst(getString(R.string.app_scheme), "https"));
                }
            }
        }
    }
}
