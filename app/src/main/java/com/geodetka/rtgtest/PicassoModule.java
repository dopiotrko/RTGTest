package com.geodetka.rtgtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.StatFs;
import android.util.Log;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.Executors;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;

public class PicassoModule {
    public static final String TAG = PicassoModule.class.getSimpleName();

    public Picasso getPicasso(Context context) {
        OkHttpClient okHttpClient = providesOkHttpClient(context, new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build());
        OkHttp3Downloader okHttpDownloader = providesPicassoOkHttpClient(okHttpClient);
        Picasso picasso = providesCustomPicasso(context, okHttpDownloader);
        return picasso;
    }

    Picasso providesCustomPicasso(Context context, OkHttp3Downloader okHttpDownloader) {
        return new Picasso.Builder(context)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e(TAG, "onImageLoadFailed: uri: " + uri, exception);
                    }
                })
                .downloader(okHttpDownloader)
                .memoryCache(new LruCache(context))
                .executor(Executors.newSingleThreadExecutor())//avoid OutOfMemoryError
                .build();
    }

    public OkHttp3Downloader providesPicassoOkHttpClient(OkHttpClient okHttpClient) {
        return new OkHttp3Downloader(okHttpClient);
    }

    public OkHttpClient providesOkHttpClient(Context context,  Interceptor loggingInterceptor) {
        File cacheDir = createDefaultCacheDir(context, BIG_CACHE_PATH);
        long cacheSize = calculateDiskCacheSize(cacheDir);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new DefaultHeadersInterceptor(context))
                .addInterceptor(new DefaultHeadersInterceptor(context))
                .addInterceptor(loggingInterceptor)
                .cache(new Cache(cacheDir, cacheSize))
                .build();
    }

    private static final String BIG_CACHE_PATH = "picasso-big-cache";
    private static final int MIN_DISK_CACHE_SIZE = 16 * 1024 * 1024; // 16MB
    private static final int MAX_DISK_CACHE_SIZE = 512 * 1024 * 1024; // 512MB
    private static final float MAX_AVAILABLE_SPACE_USE_FRACTION = 0.9f;

    private static final float MAX_TOTAL_SPACE_USE_FRACTION = 0.25f;

    private static File createDefaultCacheDir(Context context, String path) {
        File cacheDir = context.getApplicationContext().getExternalCacheDir();
        if (cacheDir == null)
            cacheDir = context.getApplicationContext().getCacheDir();
        File cache = new File(cacheDir, path);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }

    /**
     * Calculates bonded min max cache size. Min value is
     * {@link #MIN_DISK_CACHE_SIZE}
     *
     * @param dir cache dir
     * @return disk space in bytes
     */
    private static long calculateDiskCacheSize(File dir) {
        long size = Math.min(calculateAvailableCacheSize(dir), MAX_DISK_CACHE_SIZE);
        return Math.max(size, MIN_DISK_CACHE_SIZE);
    }


    /**
     * Calculates minimum of available or total fraction of disk space
     *
     * @param dir
     * @return space in bytes
     */
    @SuppressLint("NewApi")
    private static long calculateAvailableCacheSize(File dir) {
        long size = 0;
        try {
            StatFs statFs = new StatFs(dir.getAbsolutePath());
            long totalBytes;
            long availableBytes;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
                long blockSize = statFs.getBlockSizeLong();
                availableBytes = statFs.getAvailableBlocksLong() * blockSize;
                totalBytes = statFs.getBlockCountLong() * blockSize;
            } else {
                availableBytes = statFs.getAvailableBytes();
                totalBytes = statFs.getTotalBytes();
            }
            // Target at least 90% of available or 25% of total space
            size = (long) Math.min(availableBytes * MAX_AVAILABLE_SPACE_USE_FRACTION, totalBytes
                    * MAX_TOTAL_SPACE_USE_FRACTION);
        } catch (IllegalArgumentException ignored) {
            // ignored
        }
        return size;
    }

}
