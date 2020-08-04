package ohadcn.appify;

import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;

public class AppifyWebChromeClient extends WebChromeClient {
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, true);
    }
}
