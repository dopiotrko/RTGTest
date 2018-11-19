package com.geodetka.rtgtest;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Dallin on 3/29/16.
 */
public class DefaultHeadersInterceptor implements Interceptor {
    private Context mContext;

    public DefaultHeadersInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder builder = request.newBuilder();
        builder.header("Accept", "application/json")
                .header("X-Request-ID", UUID.randomUUID().toString())
                .header("X-Client-Id", getPackageName() + "-" + getVersionName() + "-" + getVersionCode() + "-" + getBuildType())
                .header("User-Agent", getPackageName() + "-" + getVersionName() + "-" + getVersionCode() + "-" + getBuildType())
        ;

        return chain.proceed(builder.build());
    }

    private String getPackageName() {
        return mContext.getPackageName();
    }

    private String getVersionName() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "na";
    }

    private String getVersionCode() {
        try {
            return String.valueOf(mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "na";
    }

    private String getBuildType() {
        boolean isDebuggable = (0 != (mContext.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE));
        if (isDebuggable) {
            return "debug";
        } else {
            return "release";
        }
    }

}
