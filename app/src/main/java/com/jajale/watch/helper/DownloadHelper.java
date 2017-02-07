package com.jajale.watch.helper;

import android.content.Context;
import android.text.TextUtils;

import com.jajale.watch.utils.FileUtils;
import com.jajale.watch.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by athena on 2015/11/28.
 * Email: lizhiqiang@bjjajale.com
 */
public class DownloadHelper {

    public DownloadHelper(Context context) {
    }

    public DownloadHelper() {
    }

    public interface OnProgress {
        void update(int progress);

        void onFail();
    }

    public void download(final String urlS, final String filePath, final OnProgress onProgress) throws Exception {
        if (!TextUtils.isEmpty(urlS)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File file = FileUtils.initFile(filePath);
                        URL url = new URL(urlS);
                        L.e("file--->" + filePath);
                        URLConnection connection = url.openConnection();
                        InputStream is = connection.getInputStream();
                        OutputStream os = new FileOutputStream(file);
                        int len = 0;
                        int prog = 0;
                        int total = connection.getContentLength();
                        byte[] buffer = new byte[1024];
                        while ((len = is.read(buffer)) > 0) {
                            prog = prog + len;
                            os.write(buffer, 0, len);
                            if (onProgress != null) {
                                if ((prog * 100 / total) < 100)
                                    onProgress.update((prog * 100 / total));
                            }
                        }
                        os.close();
                        is.close();
                        if (onProgress != null) {
                            onProgress.update(100);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (onProgress != null) {
                            onProgress.onFail();
                        }
                    }
                }
            }).start();
        } else {
            L.i("guokm","url是null");
            throw new NullPointerException("url 是空");
        }
    }
}
