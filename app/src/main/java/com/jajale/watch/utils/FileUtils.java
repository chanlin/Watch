package com.jajale.watch.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 操作文件的静态方法
 */
public class FileUtils {
    /**
     * 移动文件
     *
     * @param oldPath 原文件的完整地址
     * @param newPath 目的文件完整地址
     */
    public static boolean moveFile(final String oldPath, final String newPath) {
        boolean success = copyFile(oldPath, newPath);
//        if (success) {
//            return new File(oldPath).delete();
//        }
        return false;
    }

    public static boolean copyAssets(Context context, String filename, String targetPath) {

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = context.getAssets().open(filename);
            File newF = new File(targetPath);
            if (newF.exists()) {
                newF.delete();
            }
            outputStream = new FileOutputStream(newF);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            L.e("拷贝成长数据库IOExceptionIOException11++"+e.toString());
            e.printStackTrace();
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 拷贝文件
     *
     * @param oldPath 原文件的完整地址
     * @param newPath 目的文件完整地址
     * @return 是否成功
     */
    public static boolean copyFile(final String oldPath, final String newPath) {

        final File oldF = new File(oldPath);
        if (oldF.exists()) {
            try {

                File newF = new File(newPath);
                if (newF.exists()) {
                    newF.delete();
                }
                InputStream inStream = new FileInputStream(oldPath);
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, length);
                }
                fs.close();
                inStream.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static File initFile(String filename) {
        File file = null;
        try {
            file = new File(filename);
            if (file.exists()) {
                file.delete();
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.getParentFile().mkdirs();
                } else {

                }
                parentFile.mkdirs();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//    public static boolean copy2DB(Context activity, String filePath, String fileName) {
//        File oldfile = new File(filePath, fileName);
//        if (oldfile.exists()) {
//            oldfile.delete();
//        }
//        oldfile.getParentFile().mkdirs();
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//            out = new FileOutputStream(oldfile);
//            byte[] buff = new byte[1024];
//            int len = 0;
//            in = activity.getAssets().open(fileName);
//            while ((len = in.read(buff)) > 0) {
//                out.write(buff, 0, len);
//            }
//
//            out.flush();
//            in.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            try {
//                if (out != null)
//                    out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                return false;
//            }
//        }
//        return true;
//    }

//    public static void writeString2File(Context context, String filename, String str) {
//        writeString2File(context, filename, str, false);
//    }

    /**
     * 保存字符串到文件，会覆盖
     */
    public static void writeString2File(Context context, String filename, String str, boolean append) {
        // 得到当前外部存储设备的目录
        // 获取SdCard状态
        String state = android.os.Environment.getExternalStorageState();
        String rootDir = null;

        // 判断SdCard是否存在并且是可用的
        if (android.os.Environment.MEDIA_MOUNTED.equals(state)) {
            if (android.os.Environment.getExternalStorageDirectory().canWrite()) {
                rootDir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
            }
        }
        if (rootDir == null) {
            rootDir = "/mnt/sdcard/";
//            rootDir = context.getFilesDir() + File.separator;
        }

        String filepath = rootDir + filename;
        OutputStreamWriter outputStreamWriter = null;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    parentFile.getParentFile().mkdirs();
                    parentFile.mkdirs();
                }
                file.createNewFile();
            }
            outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file, append));
            outputStreamWriter.write(str);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStreamWriter != null) {
                    outputStreamWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void writeString2File(String string, String filename) throws IOException {
        File file = initFile(filename);
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(string.getBytes());
        outputStream.close();
    }

    public static String readLine(String filename) throws IOException {

        File file = new File(filename);
        if (file.exists()) {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String readLine = bufferedReader.readLine();
            return readLine;
        }
        return null;
    }


    public static byte[] getByteArray(String fileName) {
        String result = "";

        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        FileReader fileReader;
        BufferedReader reader;
        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);

            String line = "";
            while ((line = reader.readLine()) != null) {
                result += line;
            }

            fileReader.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.getBytes();
    }



    /**
     * 判断SD是否可以
     *
     * @return
     */
    public static boolean isSdcardExist() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }

    /**
     * 创建根目录
     *
     * @param path
     *            目录路径
     */
    public static void createDirFile(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * 创建文件
     *
     * @param path
     *            文件路径
     * @return 创建的文件
     */
    public static File createNewFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                return null;
            }
        }
        return file;
    }

    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }
}
